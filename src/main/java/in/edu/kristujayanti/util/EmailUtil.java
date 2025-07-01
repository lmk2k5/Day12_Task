package in.edu.kristujayanti.util;

import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;

public class EmailUtil {
    public static void sendEmail(Vertx vertx, String to, String subject, String body) {
        MailConfig config = new MailConfig()
                .setHostname("smtp.gmail.com")
                .setPort(587)
                .setStarttls(true)
                .setUsername("your-email@gmail.com")
                .setPassword("your-app-password");

        MailClient client = MailClient.createShared(vertx, config);

        MailMessage message = new MailMessage()
                .setFrom("your-email@gmail.com")
                .setTo(to)
                .setSubject(subject)
                .setText(body);

        client.sendMail(message)
                .onSuccess(res -> System.out.println("✅ Email sent to " + to))
                .onFailure(err -> System.err.println("❌ Email failed: " + err.getMessage()));
    }
}
