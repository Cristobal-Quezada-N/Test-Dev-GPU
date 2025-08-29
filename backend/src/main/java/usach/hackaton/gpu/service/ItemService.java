package usach.hackaton.gpu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.Item;
import usach.hackaton.gpu.repositories.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item saveItem(Item item) {
        if(item.getStock() <= 0){
            item.setAvailable(false);
        }
        else {
            item.setAvailable(item.getAvailable());
        }

        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Optional<Item> updateItem(Long id, Item updatedItem) {
        return itemRepository.findById(id).map(item -> {
            item.setName(updatedItem.getName());
            item.setStock(updatedItem.getStock());
            item.setAvailable(updatedItem.getAvailable());

            if(updatedItem.getStock() <= 0){
                item.setAvailable(false);
            }
            else {
                item.setAvailable(updatedItem.getAvailable());
            }

            return itemRepository.save(item);
        });
    }
}
