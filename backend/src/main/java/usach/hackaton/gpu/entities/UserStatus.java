package usach.hackaton.gpu.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import usach.hackaton.gpu.enums.UserStatusCode;

@Entity
@Table(name = "user_status")
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    public boolean hasCode(UserStatusCode statusCode) {
        return statusCode != null && statusCode.name().equals(this.code);
    }

    public boolean isActive() {
        return hasCode(UserStatusCode.ACTIVE);
    }

    public boolean isPending() {
        return hasCode(UserStatusCode.PENDING);
    }

    public boolean isBanned() {
        return hasCode(UserStatusCode.BANNED);
    }
}
