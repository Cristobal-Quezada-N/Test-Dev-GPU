package usach.hackaton.gpu.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "loan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "status_id", nullable = false)
    private Long statusId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalDate deadline;
}
