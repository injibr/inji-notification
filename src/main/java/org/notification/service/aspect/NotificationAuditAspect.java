package org.notification.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.notification.service.config.AuditConfig;
import org.notification.service.dto.NotifyRequest;
import org.notification.service.entity.NotificationAudit;
import org.notification.service.repository.NotificationAuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
public class NotificationAuditAspect {
    @Autowired
    private NotificationAuditRepository auditRepository;
    @Autowired
    private AuditConfig auditConfig;
    private static final Logger logger = LoggerFactory.getLogger(NotificationAuditAspect.class);

    @Around(
            "execution(* org.notification.service.controller.NotifyController.notify(..)) && args(bankId, bankSecret, request)"
    )
    public Object auditNotifyRequest(ProceedingJoinPoint pjp, String bankId, String bankSecret, NotifyRequest request) throws Throwable {
        // 🔥 If audit disabled → skip everything
        if (!auditConfig.isAuditEnabled()) {
            return pjp.proceed();
        }
        NotificationAudit audit = new NotificationAudit();
        audit.setIssuedBy(request.getCpfNumber());
        audit.setCreatedTime(LocalDateTime.now());
        String correlationId = UUID.randomUUID() + "-" + System.currentTimeMillis();
        audit.setCorrelationId(correlationId);

        String verificationLink = (request.getRequest() != null && request.getRequest().getData() != null)
                ? request.getRequest().getData().get("verificationLink") : null;
        if (verificationLink != null) {
            VerificationLinkInfo info = VerificationLinkDecoder.decode(verificationLink);
            audit.setRequestId(info.getRequestId());
            audit.setVcs(info.getVcs());
        }

        boolean isSent = false;
        Object response = null;
        try {
            response = pjp.proceed();
            if (response instanceof org.springframework.http.ResponseEntity) {
                org.springframework.http.ResponseEntity<?> resp = (org.springframework.http.ResponseEntity<?>) response;
                isSent = resp.getStatusCode().is2xxSuccessful();
            }
        } catch (Exception ex) {
            logger.error("Error sending notification for correlationId {}: and requestId {}:", correlationId, audit.getRequestId(), ex);
            audit.setNotificationSent(isSent);
            auditRepository.save(audit);
            throw ex;
        }
        audit.setNotificationSent(isSent);
        audit.setIssuedTime(LocalDateTime.now());
        logger.info("Added audit logs for correlationId {}: and requestId {}:", correlationId, audit.getRequestId());
        auditRepository.save(audit);
        return response;
    }
}
