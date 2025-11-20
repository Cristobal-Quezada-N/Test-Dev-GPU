package usach.hackaton.gpu.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.ItemCopy;

@Repository
public interface ItemCopyRepository extends JpaRepository<ItemCopy, Long> {
    List<ItemCopy> findByItemId(Long itemId);

    @Query("SELECT IC FROM ItemCopy IC WHERE IC.item.id = :itemId AND IC.status = 'AVAILABLE'")
    List<ItemCopy> findAvailableByItemId(@Param("itemId") Long itemId);

    Optional<ItemCopy> findByCopyNumber(String copyNumber);
}
