package org.notification.service.dto;

import java.util.Map;

public class NotifyRequest {
    private String bankId;
    private String cpf;
    private String requestId;
    private String transactionId;
    private String authorizationRequest;
    private String notificationType;
    private String priority;
    private String titulo;
    private String corpoDaMensagem;

    public String getBankId() { return bankId; }
    public void setBankId(String bankId) { this.bankId = bankId; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getAuthorizationRequest() { return authorizationRequest; }
    public void setAuthorizationRequest(String authorizationRequest) { this.authorizationRequest = authorizationRequest; }
    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getCorpoDaMensagem() { return corpoDaMensagem; }
    public void setCorpoDaMensagem(String corpoDaMensagem) { this.corpoDaMensagem = corpoDaMensagem; }
}
