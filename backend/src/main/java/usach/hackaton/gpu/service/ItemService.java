package usach.hackaton.gpu.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemCopyRepository itemCopyRepository;

    @Transactional
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public Item createItem(Item item) {
        item.setCreatedAt(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);

        log.debug("Item created: id = {}, name = {}", savedItem.getId(), savedItem.getName());
        return savedItem;
    }

    @Transactional(readOnly = true)
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Item> getAllAvailableItems() {
        return itemRepository.findAllWithAvailableCopies();
    }

    @Transactional(readOnly = true)
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Item getItemByIdOrThrow(Long id) {
        return itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Item> getItemsByCategory(String category) {
        return itemRepository.findByCategory(category);
    }

    @Transactional
    public Item updateItem(Long id, Item updatedItem) {
        Item item = getItemByIdOrThrow(id);

        item.setName(updatedItem.getName());
        item.setDescription(updatedItem.getDescription());
        item.setCategory(updatedItem.getCategory());
        item.setMinPeople(updatedItem.getMinPeople());
        item.setMaxUsageMinutes(updatedItem.getMaxUsageMinutes());
        item.setImageURL(updatedItem.getImageURL());

        Item itemSaved = itemRepository.save(item);
        log.debug("Item updated: id = {}, name = {}", item.getId(), item.getName());

        return itemSaved;
    }

    @Transactional
    public void deleteItem(Long id) {
        Item item = getItemByIdOrThrow(id);

        long loanedCopies = item.getCopies().stream()
            .filter(copy -> "LOANED".equals(copy.getStatus()))
            .count();

        if (loanedCopies > 0) {
            throw new RuntimeException("Cannot delete item: has {} loadned copies" + loanedCopies);
        }
        itemRepository.deleteById(id);
        log.debug("Item deleted: id = {}, name = {}", id, item.getName());
    }

    @Transactional(readOnly = true)
    public List<ItemCopy> getAvailableCopies(Item item) {
        return itemCopyRepository.findAvailableByItemId(item.getId());
    }
}
