package usach.hackaton.gpu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
