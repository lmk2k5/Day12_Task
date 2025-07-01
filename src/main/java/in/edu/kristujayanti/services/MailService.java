package in.edu.kristujayanti.services;

import in.edu.kristujayanti.config.Config;  // Add this import
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;

public class MailService {
    private final MailClient mailClient;
    private final String smtpUser;

    public MailService(Vertx vertx) {
        MailConfig config = new MailConfig()
                .setHostname(Config.SMTP_HOST)
                .setPort(Config.SMTP_PORT)
                .setUsername(Config.SMTP_USER)
                .setPassword(Config.SMTP_PASS)
                .setSsl(true);

        this.mailClient = MailClient.create(vertx, config);
        this.smtpUser = Config.SMTP_USER;
    }

    public Future<Void> sendPasswordEmail(String toEmail, String password) {
        MailMessage message = new MailMessage()
                .setFrom(smtpUser)
                .setTo(toEmail)
                .setSubject("Your Event Ticket System Password")
                .setText("Your password is: " + password);

        return mailClient.sendMail(message).mapEmpty();
    }

    public Future<Void> sendBookingConfirmation(String userEmail, String eventId, String token) {
        MailMessage message = new MailMessage()
                .setFrom(smtpUser)
                .setTo(userEmail)
                .setSubject("Your Event Booking Confirmation")
                .setText("Booking Details:\n\n" +
                        "Event ID: " + eventId + "\n" +
                        "Your Token: " + token + "\n\n" +
                        "Present this token at the event entrance.");

        return mailClient.sendMail(message).mapEmpty();
    }
}