package org.notification.service.service.impl;

import org.notification.service.dto.NotifyRequest;
import org.notification.service.exception.NotificationSendException;
import org.notification.service.service.NotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotifyServiceImpl implements NotifyService {
    private static final Logger logger = LoggerFactory.getLogger(NotifyServiceImpl.class);

    @Override
    public void notify(NotifyRequest request) {
        if (request.getCpfNumber() == null || request.getRequest() == null) {
            logger.warn("cpfNumber or request missing in NotifyRequest");
            throw new IllegalArgumentException("cpfNumber and request required");
        }
        // TODO: integrate push notification provider (WSO2)
        throw new NotificationSendException("Push notification provider not configured", null);
    }
}
