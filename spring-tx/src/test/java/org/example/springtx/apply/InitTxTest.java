package org.example.springtx.apply;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class InitTxTest {

  @Autowired
  Hello hello;

  @Test
  void go() {
    //초기화 코드는 스프링이 초기화 시점에 호출
    //직접 호출하면 트랜잭션 적용
    //hello.initV1();
  }

  @TestConfiguration
  static class InitTxConfig {

    @Bean
    Hello hello() {
      return new Hello();
    }
  }

  @Slf4j
  static class Hello {

    //초기화 코드가 먼저 실행되고 그 이후에 AOP 적용
    @PostConstruct
    @Transactional
    public void initV1() {
      boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
      log.info("Hello init @PostConstruct tx active={}", txActive);
    }

    @EventListener(value = ApplicationReadyEvent.class)
    @Transactional
    public void initV2() {
      boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
      log.info("Hello init ApplicationReadyEvent tx active={}", txActive);
    }
  }
}
