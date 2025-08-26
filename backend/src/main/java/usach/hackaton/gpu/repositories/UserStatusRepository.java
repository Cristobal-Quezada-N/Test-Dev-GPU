package usach.hackaton.gpu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.UserStatus;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
}
