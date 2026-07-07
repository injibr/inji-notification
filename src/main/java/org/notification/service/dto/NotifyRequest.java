package org.notification.service.dto;

import java.util.Map;

public class NotifyRequest {
    private String cpfNumber;
    private Request request;

    public String getCpfNumber() { return cpfNumber; }
    public void setCpfNumber(String cpfNumber) { this.cpfNumber = cpfNumber; }
    public Request getRequest() { return request; }
    public void setRequest(Request request) { this.request = request; }

    public static class Request {
        private Notification notification;
        private Map<String, String> data;

        public Notification getNotification() { return notification; }
        public void setNotification(Notification notification) { this.notification = notification; }
        public Map<String, String> getData() { return data; }
        public void setData(Map<String, String> data) { this.data = data; }
    }

    public static class Notification {
        private String title;
        private String body;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
    }
}
