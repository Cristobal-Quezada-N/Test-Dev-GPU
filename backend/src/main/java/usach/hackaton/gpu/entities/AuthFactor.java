package usach.hackaton.gpu.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "auth_factor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AuthFactorType type;

    private boolean isUsed;

    private LocalDate creationDate;

    private LocalDate expirationDate;

    // Many To One
    @Column(nullable = false, name = "user_id")
    private String userId;
}
