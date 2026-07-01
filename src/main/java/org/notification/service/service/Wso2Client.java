package org.notification.service.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.notification.service.dto.MirNotificationRequest;
import org.notification.service.exception.NotificationSendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Wso2Client {

    private static final Logger log = LoggerFactory.getLogger(Wso2Client.class);

    private final WebClient tokenWebClient;
    private final WebClient mirWebClient;

    @Value("${wso2.token.client-id}")
    private String clientId;

    @Value("${wso2.token.client-secret}")
    private String clientSecret;

    @Value("${wso2.mir.notification-path}")
    private String notificationPath;

    public Wso2Client(
            @Value("${wso2.token.url}") String tokenUrl,
            @Value("${wso2.mir.base-url}") String mirBaseUrl) {
        this.tokenWebClient = WebClient.builder().baseUrl(tokenUrl).build();
        this.mirWebClient = WebClient.builder().baseUrl(mirBaseUrl).build();
    }

    public void sendNotification(MirNotificationRequest request) {
        String token = getAccessToken();
        log.info("WSO2 token retrieved, sending notification for CPFs: {}", request.getCpfs());

        mirWebClient.post()
                .uri(notificationPath)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), response ->
                        response.bodyToMono(String.class).map(body -> {
                            log.error("MIR API error - status: {}, body: {}", response.statusCode(), body);
                            return new NotificationSendException("MIR API returned error: " + response.statusCode() + " - " + body);
                        })
                )
                .bodyToMono(Void.class)
                .block();

        log.info("Notification sent successfully via WSO2 MIR");
    }

    private String getAccessToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        JsonNode response = tokenWebClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), res ->
                        res.bodyToMono(String.class).map(body -> {
                            log.error("Failed to retrieve WSO2 token: {}", body);
                            return new NotificationSendException("Failed to retrieve WSO2 token: " + body);
                        })
                )
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null || !response.has("access_token")) {
            throw new NotificationSendException("WSO2 token response missing access_token");
        }
        return response.get("access_token").asText();
    }
}
