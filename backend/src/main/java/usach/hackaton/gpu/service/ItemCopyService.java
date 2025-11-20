package usach.hackaton.gpu.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usach.hackaton.gpu.entities.Item;
import usach.hackaton.gpu.entities.ItemCopy;
import usach.hackaton.gpu.repositories.ItemCopyRepository;
import usach.hackaton.gpu.repositories.ItemRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemCopyService {
    private final ItemRepository itemRepository;
    private final ItemCopyRepository itemCopyRepository;

    @Transactional
    public ItemCopy save(ItemCopy itemCopy) {
        return itemCopyRepository.save(itemCopy);
    }

    @Transactional
    public ItemCopy createCopy(Long itemId, ItemCopy itemCopy) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new RuntimeException("Item not found: id = " + itemId));

        itemCopy.setItem(item);
        if (itemCopy.getAcquisitionDate() == null) {
            itemCopy.setAcquisitionDate(LocalDate.now());
        }

        ItemCopy itemCopySaved = itemCopyRepository.save(itemCopy);
        log.debug(
            "ItemCopy created: id = {}, copyNumber = {}, item = {}",
            itemCopySaved.getId(), itemCopySaved.getCopyNumber(), itemCopySaved.getItem().getName()
        );
        return itemCopySaved;
    }

    @Transactional(readOnly = true)
    public ItemCopy getItemCopyById(Long itemCopyId) {
        return itemCopyRepository.findById(itemCopyId)
            .orElseThrow(() -> new RuntimeException("ItemCopy not found: " + itemCopyId));
    }

    @Transactional(readOnly = true)
    public List<ItemCopy> getAllCopies() {
        return itemCopyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ItemCopy> getCopiesByItemId(Long itemId) {
        return itemCopyRepository.findByItemId(itemId);
    }

    @Transactional
    public ItemCopy updateCopy(ItemCopy itemCopy, ItemCopy updatedItemCopy) {
        itemCopy.setCopyNumber(updatedItemCopy.getCopyNumber());
        itemCopy.setCondition(updatedItemCopy.getCondition());
        itemCopy.setStatus(updatedItemCopy.getStatus());
        itemCopy.setNotes(updatedItemCopy.getNotes());

        ItemCopy itemCopySaved = itemCopyRepository.save(itemCopy);
        log.debug("ItemCopy updated: id = {}, copyNumber = {}", itemCopySaved.getId(), itemCopySaved.getCopyNumber());

        return itemCopySaved;
    }

    @Transactional
    public ItemCopy markAsLoaned(Long itemCopyId) {
        ItemCopy itemCopy = getItemCopyById(itemCopyId);

        if (!itemCopy.isAvailable()) {
            throw new RuntimeException("ItemCopy is not available for loan: " + itemCopy.getCopyNumber());
        }

        itemCopy.setStatus("LOANED");

        ItemCopy itemCopySaved = itemCopyRepository.save(itemCopy);
        log.debug(
            "ItemCopy marked as LOANED: id = {}, copyNumber = {}", itemCopySaved.getId(), itemCopySaved.getCopyNumber()
        );

        return itemCopySaved;
    }

    @Transactional
    public ItemCopy markAsDamaged(Long copyId, String reason) {
        ItemCopy copy = getItemCopyById(copyId);
        copy.setCondition("DAMAGED");
        copy.setStatus("MAINTENANCE");
        copy.setNotes(reason);

        ItemCopy saved = itemCopyRepository.save(copy);
        log.debug("ItemCopy marked as DAMAGED: id={}, reason={}", copyId, reason);
        return saved;
    }

    @Transactional
    public ItemCopy markAsLost(Long copyId) {
        ItemCopy copy = getItemCopyById(copyId);
        copy.setCondition("LOST");
        copy.setStatus("RETIRED");

        ItemCopy saved = itemCopyRepository.save(copy);
        log.debug("ItemCopy marked as LOST: id={}, copyNumber={}", copyId, copy.getCopyNumber());
        return saved;
    }

    @Transactional
    public ItemCopy updateCondition(Long copyId, String newCondition, String notes) {
        ItemCopy copy = getItemCopyById(copyId);
        copy.setCondition(newCondition);
        copy.setNotes(notes);

        ItemCopy saved = itemCopyRepository.save(copy);
        log.info("ItemCopy condition updated: id={}, condition={}", copyId, newCondition);
        return saved;
    }

    @Transactional
    public void deleteCopy(Long itemCopyId) {
        ItemCopy itemCopy = getItemCopyById(itemCopyId);

        if (itemCopy.getStatus() == "LOANED") {
            throw new IllegalStateException("Cannot delete a loaned copy: " + itemCopy.getCopyNumber());
        }

        itemCopyRepository.deleteById(itemCopyId);
        log.debug("ItemCopy deleted: id={}, copyNumber={}", itemCopyId, itemCopy.getCopyNumber());
    }

    @Transactional(readOnly = true)
    public List<ItemCopy> getAvailableCopies(Long itemId) {
        return itemCopyRepository.findAvailableByItemId(itemId);
    }

    @Transactional(readOnly = true)
    public ItemCopy getNextAvailableCopy(Long itemId) {
        List<ItemCopy> availableItemCopies = getAvailableCopies(itemId);

        if (availableItemCopies.isEmpty()) {
            throw new RuntimeException("No available copies left for item: " + itemId);
        }

        return availableItemCopies.stream()
            .filter(copy -> copy.getCondition() == "GOOD")
            .findFirst()
            .orElse(availableItemCopies.get(0));
    }
}
