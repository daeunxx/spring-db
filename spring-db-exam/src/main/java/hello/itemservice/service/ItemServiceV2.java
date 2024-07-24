package hello.itemservice.service;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import hello.itemservice.repository.v2.ItemQueryRepositoryV2;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceV2 implements ItemService{

  private final ItemRepository itemRepository;
  private final ItemQueryRepositoryV2 itemQueryRepository;

  @Override
  public Item save(Item item) {
    return itemRepository.save(item);
  }

  @Override
  public void update(Long itemId, ItemUpdateDto updateParam) {
    Item findItem = findById(itemId).orElseThrow();
    findItem.setItemName(updateParam.getItemName());
    findItem.setPrice(updateParam.getPrice());
    findItem.setQuantity(updateParam.getQuantity());
  }

  @Override
  public Optional<Item> findById(Long id) {
    return itemRepository.findById(id);
  }

  @Override
  public List<Item> findItems(ItemSearchCond itemSearch) {
    return itemQueryRepository.findAll(itemSearch);
  }
}
