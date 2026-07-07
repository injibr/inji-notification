package org.notification.service.service.impl;

import org.notification.service.dto.MirNotificationRequest;
import org.notification.service.dto.NotifyRequest;
import org.notification.service.exception.NotificationSendException;
import org.notification.service.service.NotifyService;
import org.notification.service.service.Wso2Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class NotifyServiceImpl implements NotifyService {

    private static final Logger logger = LoggerFactory.getLogger(NotifyServiceImpl.class);

    private static final String TIPO_FONTE = "INJI";
    private static final String ACAO = "SOLICITACAO_VCS";

    private final Wso2Client wso2Client;

    public NotifyServiceImpl(Wso2Client wso2Client) {
        this.wso2Client = wso2Client;
    }

    @Override
    public void notify(NotifyRequest request, String bankId) {
        if (request.getCpfNumber() == null) {
            throw new IllegalArgumentException("cpfNumber is required");
        }

        String titulo = request.getRequest() != null && request.getRequest().getNotification() != null
                ? request.getRequest().getNotification().getTitle() : "Solicitação de Verificação";
        String corpo = request.getRequest() != null && request.getRequest().getNotification() != null
                ? request.getRequest().getNotification().getBody() : "Você possui uma nova solicitação de verificação de credenciais.";

        String authorizationRequest = null;
        if (request.getRequest() != null && request.getRequest().getData() != null) {
            authorizationRequest = request.getRequest().getData().get("verificationLink");
        }

        Map<String, Object> dados = new HashMap<>();
        dados.put("bankId", bankId != null && !bankId.isBlank() ? bankId : "unknown_bank");
        dados.put("requestId", UUID.randomUUID().toString());
        dados.put("transactionId", bankId + "_txn_" + System.currentTimeMillis());
        dados.put("authorizationRequest", authorizationRequest);
        dados.put("notificationType", "VP_REQUEST");
        dados.put("priority", "high");

        MirNotificationRequest mirRequest = new MirNotificationRequest(
                titulo, corpo, List.of(request.getCpfNumber()), TIPO_FONTE, ACAO, dados, true
        );

        try {
            wso2Client.sendNotification(mirRequest);
        } catch (NotificationSendException e) {
            logger.error("Error sending notification for CPF {}: {}", request.getCpfNumber(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error sending notification for CPF {}: {}", request.getCpfNumber(), e.getMessage(), e);
            throw new NotificationSendException("Unexpected error: " + e.getMessage(), e);
        }

        logger.info("Notification sent for CPF: {}", request.getCpfNumber());
    }
}
