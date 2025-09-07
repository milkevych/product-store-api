package org.projects.notificationservice.store.entity;

import lombok.Getter;

public enum EmailTemplate {

    ORDER_CONFIRMATION("order-confirmation.html", "Order confirmation"),
    PAYMENT_NOTIFICATION("payment-notification.html", "Payment successfully processed");


    @Getter
    private final String template;
    @Getter
    private final String subject;

    EmailTemplate(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
