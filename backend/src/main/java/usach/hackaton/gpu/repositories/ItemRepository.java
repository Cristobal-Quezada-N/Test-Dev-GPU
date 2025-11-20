package usach.hackaton.gpu.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByCategory(String category);

    @Query("""
            SELECT DISTINCT i FROM Item i
            LEFT JOIN FETCH i.copies c
            WHERE EXISTS (
                SELECT 1 FROM ItemCopy ic
                WHERE ic.item = i
                AND ic.status = 'AVAILABLE'
            )
        """)
    List<Item> findAllWithAvailableCopies();
}
