package com.eventix.Notification.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Email Service that sends booking confirmation emails using SendGrid API.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    /**
     * Sends a booking confirmation email to the user.
     *
     * @param toEmail   The recipient's email address
     * @param name      The recipient's name
     * @param eventName The name of the event booked
     * @param bookingId The unique booking ID
     * @throws IOException if the email fails to send
     */
    public void sendBookingConfirmation(String toEmail, String name, String eventName, String bookingId) throws IOException {

        logger.info("Preparing to send booking confirmation email to: {}", toEmail);

        // Create sender and recipient email objects
        Email from = new Email(fromEmail, "Eventix Team");
        Email to = new Email(toEmail);
        String subject = "Your Booking is Confirmed! (ID: " + bookingId + ")";

        // Build HTML email content
        String htmlContent = String.format(
            "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
            "<h2 style='color: #4CAF50;'>Booking Confirmation</h2>" +
            "<h3>Hi %s,</h3>" +
            "<p>Great news! Your booking for <strong>%s</strong> has been confirmed!</p>" +
            "<div style='background-color: #f5f5f5; padding: 15px; border-radius: 5px; margin: 20px 0;'>" +
            "<p style='margin: 5px 0;'><strong>Booking ID:</strong> %s</p>" +
            "<p style='margin: 5px 0;'><strong>Event:</strong> %s</p>" +
            "</div>" +
            "<p>We look forward to seeing you there!</p>" +
            "<p style='color: #666; font-size: 12px; margin-top: 30px;'>If you have any questions, please contact our support team.</p>" +
            "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>" +
            "<p style='color: #999; font-size: 11px;'>This is an automated message from Eventix. Please do not reply to this email.</p>" +
            "</div>",
            name, eventName, bookingId, eventName
        );

        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        // Send the email via SendGrid
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            // Check response status
            if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
                logger.error("SendGrid Error Response - Status: {}, Body: {}",
                    response.getStatusCode(), response.getBody());
                throw new IOException("Failed to send email, status code: " + response.getStatusCode());
            }

            logger.info("Email sent successfully to {} - Status: {}", toEmail, response.getStatusCode());

        } catch (IOException e) {
            logger.error("Failed to send email to {}: {}", toEmail, e.getMessage(), e);
            throw e;
        }
    }
}

