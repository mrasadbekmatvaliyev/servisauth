package com.example.user_auth.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_codes")
public class OtpCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiresAt;
    private boolean isUsed = false;

    public OtpCode() {}

    public OtpCode(User user, String code, LocalDateTime expiresAt) {
        this.user = user;
        this.code = code;
        this.expiresAt = expiresAt;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getCode() { return code; }
    public User getUser() { return user; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public boolean isUsed() { return isUsed; }

    public void setCode(String code) { this.code = code; }
    public void setUser(User user) { this.user = user; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public void setUsed(boolean used) { isUsed = used; }
}
