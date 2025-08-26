package usach.hackaton.gpu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.AppUser;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

}
