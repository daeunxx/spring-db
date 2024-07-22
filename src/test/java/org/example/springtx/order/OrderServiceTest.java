package org.example.springtx.order;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class OrderServiceTest {

  @Autowired
  OrderService orderService;

  @Autowired
  OrderRepository orderRepository;

  @Test
  void complete() throws NotEnoughMoneyException {
    //given
    Order order = new Order();
    order.setName("정상");

    //when
    orderService.order(order);

    //then
    Order findOrder = orderRepository.findById(order.getId()).get();
    assertThat(findOrder.getPayStatus()).isEqualTo("완료");
  }

  @Test
  void runtimeException() {
    //given
    Order order = new Order();
    order.setName("예외");

    //when, then
    assertThatThrownBy(() -> orderService.order(order)).isInstanceOf(RuntimeException.class);

    //런타임 에외는 롤백
    Optional<Order> findOrder = orderRepository.findById(order.getId());
    assertThat(findOrder).isEmpty();
  }

  @Test
  void bizException() {
    //given
    Order order = new Order();
    order.setName("잔고부족");

    try {
      orderService.order(order);
      fail("잔고 부족 예외 발생");
    } catch (NotEnoughMoneyException e) {
      log.info("고객에게 잔고 부족임을 알리고 별도 계좌 입금 안내");
    }

    Order findOrder = orderRepository.findById(order.getId()).get();
    assertThat(findOrder.getPayStatus()).isEqualTo("대기");
  }
}
