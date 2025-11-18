package usach.hackaton.gpu.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usach.hackaton.gpu.enums.ActivationTokenStatusCode;

@Entity
@Table(name = "activation_token_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivationTokenStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false, unique = true)
    private String name;

    public boolean hasCode(ActivationTokenStatusCode statusCode) {
        return statusCode != null && statusCode.name().equals(this.code);
    }

    public boolean isPending() {
        return hasCode(ActivationTokenStatusCode.PENDING);
    }

    public boolean isUsed() {
        return hasCode(ActivationTokenStatusCode.USED);

    }

    public boolean isExpired() {
        return hasCode(ActivationTokenStatusCode.EXPIRED);
    }

    public boolean isRevoked() {
        return hasCode(ActivationTokenStatusCode.REVOKED);
    }
}
