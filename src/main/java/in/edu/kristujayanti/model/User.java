package in.edu.kristujayanti.model;

import io.vertx.core.json.JsonObject;

public class User {
    private String id;
    private String name;
    private String email;
    private String hashedPassword;
    private String role;
    private String resetToken;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHashedPassword() { return hashedPassword; }
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public JsonObject toJson() {
        return new JsonObject()
                .put("name", name)
                .put("email", email)
                .put("hashedPassword", hashedPassword)
                .put("role", role != null ? role : "user")
                .put("resetToken", resetToken);
    }
}