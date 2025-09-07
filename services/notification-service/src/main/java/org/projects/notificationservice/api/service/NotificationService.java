package org.projects.notificationservice.api.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.projects.notificationservice.store.entity.EmailTemplate;
import org.projects.notificationservice.store.entity.PaymentMethod;
import org.projects.notificationservice.store.entity.PaymentStatus;
import org.projects.notificationservice.store.entity.ProductPurchaseResponse;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService{

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendOrderMessage(
            String email,
            String userFullname,
            BigDecimal totalAmount,
            PaymentMethod method,
            List<ProductPurchaseResponse> products
    ) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("testcontactmail@test.com");

        final String templateName = EmailTemplate.ORDER_CONFIRMATION.getTemplate();

        Map<String,Object> model = new HashMap<>();
        model.put("userFullname", userFullname);
        model.put("totalAmount", totalAmount);
        model.put("method", method);
        model.put("products", products);

        Context context = new Context();
        context.setVariables(model);

        messageHelper.setSubject(EmailTemplate.ORDER_CONFIRMATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(email);
            mailSender.send(mimeMessage);
            log.info(String.format("Email was succesfully send to %s", email));
        } catch(MessagingException e) {
            log.warn("Cannot send email to {} ",email);
        }

    }

    @Async
    public void sendPaymentMessage(
            String userFullname, 
            String email,
            BigDecimal totalAmount, 
            PaymentMethod method, 
            PaymentStatus status
        ) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("testcontactmail@test.com");

        final String templateName = EmailTemplate.PAYMENT_NOTIFICATION.getTemplate();

        Map<String,Object> model = new HashMap<>();
        model.put("userFullname", userFullname);
        model.put("totalAmount", totalAmount);
        model.put("method", method);
        model.put("status", status);

        Context context = new Context();
        context.setVariables(model);

        messageHelper.setSubject(EmailTemplate.PAYMENT_NOTIFICATION.getSubject());

        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(email);
            mailSender.send(mimeMessage);
            log.info(String.format("Email was succesfully send to %s", email));
        } catch(MessagingException e) {
            log.warn("Cannot send email to {} ",email);
        }

    }
}
