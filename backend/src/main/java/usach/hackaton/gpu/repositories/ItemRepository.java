package usach.hackaton.gpu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
}
