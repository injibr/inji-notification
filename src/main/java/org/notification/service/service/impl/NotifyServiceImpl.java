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

@Service
public class NotifyServiceImpl implements NotifyService {

    private static final Logger logger = LoggerFactory.getLogger(NotifyServiceImpl.class);

    private static final String DEFAULT_TITULO = "Solicitação de Verificação";
    private static final String DEFAULT_CORPO = "Você possui uma nova solicitação de verificação de credenciais.";
    private static final String TIPO_FONTE = "INJI";
    private static final String ACAO = "SOLICITACAO_VCS";

    private final Wso2Client wso2Client;

    public NotifyServiceImpl(Wso2Client wso2Client) {
        this.wso2Client = wso2Client;
    }

    @Override
    public void notify(NotifyRequest request) {
        if (request.getCpf() == null) {
            logger.warn("cpf missing in NotifyRequest");
            throw new IllegalArgumentException("cpf is required");
        }

        String titulo = request.getTitulo() != null ? request.getTitulo() : DEFAULT_TITULO;
        String corpo = request.getCorpoDaMensagem() != null ? request.getCorpoDaMensagem() : DEFAULT_CORPO;

        Map<String, Object> dados = new HashMap<>();
        dados.put("bankId", request.getBankId());
        dados.put("requestId", request.getRequestId());
        dados.put("transactionId", request.getTransactionId());
        dados.put("authorizationRequest", request.getAuthorizationRequest());
        dados.put("notificationType", request.getNotificationType());
        dados.put("priority", request.getPriority());

        MirNotificationRequest mirRequest = new MirNotificationRequest(
                titulo,
                corpo,
                List.of(request.getCpf()),
                TIPO_FONTE,
                ACAO,
                dados,
                true
        );

        try {
            wso2Client.sendNotification(mirRequest);
        } catch (NotificationSendException e) {
            logger.error("Error sending notification for CPF {}: {}", request.getCpf(), e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error sending notification for CPF {}: {}", request.getCpf(), e.getMessage(), e);
            throw new NotificationSendException("Unexpected error: " + e.getMessage(), e);
        }

        logger.info("Notification sent for CPF: {}", request.getCpf());
    }
}
