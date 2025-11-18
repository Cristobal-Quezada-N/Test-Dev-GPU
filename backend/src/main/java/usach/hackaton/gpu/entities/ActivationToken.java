package usach.hackaton.gpu.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activation_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false, unique = true, length = 64)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private ActivationTokenStatus status;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    public long getRemaingHours() {
        if (hasExpired()) {
            return 0;
        }

        return Duration.between(LocalDateTime.now(), expirationDate).toHours();
    }

    public boolean hasExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }

    public boolean isAboutToExpire() {
        return isPending() && getRemaingHours() < 2;
    }

    public boolean isValid() {
        return isPending() && !hasExpired();
    }

    public boolean isPending() {
        return status != null && status.isPending();
    }

    public boolean isUsed() {
        return status != null && status.isUsed();
    }

    public boolean isExpired() {
        return status != null && status.isExpired();
    }

    public boolean isRevoked() {
        return status != null && status.isRevoked();
    }
}
