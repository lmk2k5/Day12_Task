package in.edu.kristujayanti.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private static final Properties env = new Properties();

    static {
        try {
            Files.lines(Paths.get(".env"))
                    .filter(line -> line.contains("="))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        env.setProperty(parts[0].trim(), parts[1].trim());
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load .env file", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while loading .env file", e);
        }
    }

    public static final String MONGO_URI = env.getProperty("MONGO_URI");
    public static final String JWT_SECRET = env.getProperty("JWT_SECRET");
    public static final String SMTP_HOST = env.getProperty("SMTP_HOST");
    public static final int SMTP_PORT = Integer.parseInt(env.getProperty("SMTP_PORT"));
    public static final String SMTP_USER = env.getProperty("SMTP_USER");
    public static final String SMTP_PASS = env.getProperty("SMTP_PASS");
}
