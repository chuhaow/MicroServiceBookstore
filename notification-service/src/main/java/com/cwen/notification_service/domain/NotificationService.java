package com.cwen.notification_service.domain;

import com.cwen.notification_service.ApplicationProperties;
import com.cwen.notification_service.domain.models.events.OrderCancelledEvent;
import com.cwen.notification_service.domain.models.events.OrderCreatedEvent;
import com.cwen.notification_service.domain.models.events.OrderDeliveredEvent;
import com.cwen.notification_service.domain.models.events.OrderErrorEvent;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;
    private final ApplicationProperties properties;

    public NotificationService(JavaMailSender mailSender, ApplicationProperties properties) {
        this.mailSender = mailSender;
        this.properties = properties;
    }


    public void sendOrderCreatedNotification(OrderCreatedEvent orderCreatedEvent) {
        String message = createMessageNoReason("Created", orderCreatedEvent.customer().name(),orderCreatedEvent.orderNumber());
        log.info(message);
        sendMail(orderCreatedEvent.customer().email(), "Order Created Notification", message);
    }

    public void sendOrderDeliveredNotification(OrderDeliveredEvent orderDeliveredEvent) {
        String message = createMessageNoReason("Delivered", orderDeliveredEvent.customer().name(),orderDeliveredEvent.orderNumber());
        log.info(message);
        sendMail(orderDeliveredEvent.customer().email(), "Order Delivered Notification", message);
    }

    public void sendOrderCancelledNotification(OrderCancelledEvent orderCancelledEvent) {
        String message = createMessageWithReason("Cancelled", orderCancelledEvent.customer().name(),
                orderCancelledEvent.reason(),orderCancelledEvent.orderNumber());
        log.info(message);
        sendMail(orderCancelledEvent.customer().email(), "Order Cancelled Notification", message);
    }

    public void sendOrderErrorNotification(OrderErrorEvent orderErrorEvent) {
        String message = """
                =====================================
                Order Error Notification
                -------------------------------------
                The Order: %s
                Encountered an error occurred.
                Reason: %s
                
                =====================================""".formatted(orderErrorEvent.orderNumber(), orderErrorEvent.reason());
        log.info(message);
        sendMail(properties.supportEmail(), "Order Error Notification", message);
    }

    private void sendMail(String email, String title, String message) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(properties.supportEmail());
            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(message);
            mailSender.send(mimeMessage);
            log.info("Email sent to: {}", email);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending email",e);
        }
    }

    private String createMessageNoReason(String orderState, String recipentName, String orderNumber){
        String message =
                """
                =====================================
                Order %s Notification
                -------------------------------------
                Dear %s
                Your order has been successfully %s.
                Order Number: %s
                
                Best regards,
                Company Team
                =====================================
                        """.formatted(orderState, recipentName, orderState, orderNumber);
        return message;
    }

    private String createMessageWithReason(String orderState, String recipentName, String reason, String orderNumber){
        String message =
                """
                =====================================
                Order %s Notification
                -------------------------------------
                Dear %s
                Your order has been successfully %s.
                Reason: %s
                Order Number: %s
                
                Best regards,
                Company Team
                =====================================
                        """.formatted(orderState, recipentName, orderState, reason, orderNumber);
        return message;
    }
}
