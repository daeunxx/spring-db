package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ItemMapper {

  void save(Item item);

  //파라미터가 2개 이상인 경우, @Param 사용
  void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updatePram);

  Optional<Item> findById(Long id);

  List<Item> findAll(ItemSearchCond itemSearch);
}
