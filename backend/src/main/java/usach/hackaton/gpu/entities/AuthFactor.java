package usach.hackaton.gpu.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "auth_factor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many To One
    @Column(nullable = false, name = "user_id")
    private String userId;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "type", nullable = false, columnDefinition = "auth_factor_type")
    private AuthFactorType type;

    private boolean Used;

    private LocalDate creationDate;

    private LocalDate expirationDate;
}
