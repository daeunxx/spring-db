package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

  List<Item> findByItemNameLike(String itemName);

  List<Item> findByPriceLessThanEqual(Integer price);

  List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);

  @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
  List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);
}
