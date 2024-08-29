package org.example.springtx.propagation;

import static org.assertj.core.api.Assertions.*;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Slf4j
@SpringBootTest
public class BasicTxTest {

  @Autowired
  PlatformTransactionManager txManager;

  @TestConfiguration
  public class BasicTxConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
      return new DataSourceTransactionManager(dataSource);
    }
  }

  @Test
  void commit() {
    log.info("트랜잭션 시작");
    TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

    log.info("트랜잭션 커밋 시작");
    txManager.commit(status);
    log.info("트랜잭션 커밋 완료");
  }

  @Test
  void rollback() {
    log.info("트랜잭션 시작");
    TransactionStatus status = txManager.getTransaction(new DefaultTransactionAttribute());

    log.info("트랜잭션 롤백 시작");
    txManager.rollback(status);
    log.info("트랜잭션 롤백 완료");
  }

  @Test
  void double_commit() {
    log.info("트랜잭션1 시작");
    TransactionStatus status1 = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("트랜잭션1 커밋");
    txManager.commit(status1);

    log.info("트랜잭션2 시작");
    TransactionStatus status2 = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("트랜잭션2 커밋");
    txManager.commit(status2);
  }

  @Test
  void double_commit_rollback() {
    log.info("트랜잭션1 시작");
    TransactionStatus status1 = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("트랜잭션1 커밋");
    txManager.commit(status1);

    log.info("트랜잭션2 시작");
    TransactionStatus status2 = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("트랜잭션2 롤백");
    txManager.rollback(status2);
  }

  @Test
  void inner_commit() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

    log.info("내부 트랜잭션 시작");
    TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

    log.info("내부 트랜잭션 커밋");
    txManager.commit(inner);

    log.info("외부 트랜잭션 커밋");
    txManager.commit(outer);
  }

  @Test
  void outer_only_commit() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

    log.info("내부 트랜잭션 시작");
    TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

    //처음 트랜잭션을 시작한 트랜잭션이 물리 트랜잭션 관리
    log.info("외부 트랜잭션 커밋");
    txManager.commit(outer);
  }

  @Test
  void outer_rollback() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

    log.info("내부 트랜잭션 시작");
    TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

    log.info("내부 트랜잭션 커밋");
    txManager.commit(inner);

    log.info("외부 트랜잭션 롤백");
    txManager.rollback(outer);
  }

  @Test
  void inner_rollback() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

    log.info("내부 트랜잭션 시작");
    TransactionStatus inner = txManager.getTransaction(new DefaultTransactionDefinition());
    log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

    log.info("내부 트랜잭션 롤백");
    txManager.rollback(inner);

    log.info("외부 트랜잭션 커밋");
    assertThatThrownBy(() -> txManager.commit(outer)).isInstanceOf(
        UnexpectedRollbackException.class);
  }

  @Test
  void inner_rollback_requires_new() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

    log.info("내부 트랜잭션 시작");
    DefaultTransactionDefinition definition = new DefaultTransactionAttribute();
    definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    TransactionStatus inner = txManager.getTransaction(definition);
    log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

    log.info("내부 트랜잭션 롤백");
    txManager.rollback(inner);

    log.info("외부 트랜잭션 커밋");
    txManager.commit(outer);
  }

  @Test
  void inner_rollback_requires_new2() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

    log.info("내부 트랜잭션 시작");
    DefaultTransactionDefinition definition = new DefaultTransactionAttribute();
    definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    TransactionStatus inner = txManager.getTransaction(definition);
    log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

    log.info("외부 트랜잭션 커밋");
    txManager.commit(outer);

    log.info("내부 트랜잭션 롤백");
    assertThatThrownBy(() -> txManager.rollback(inner)).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void inner_rollback_requires_new3() {
    log.info("외부 트랜잭션 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

    log.info("내부 트랜잭션 시작");
    DefaultTransactionDefinition definition = new DefaultTransactionAttribute();
    definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

    TransactionStatus inner = txManager.getTransaction(definition);
    log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

    log.info("외부 트랜잭션 롤백");
    txManager.rollback(outer);

    log.info("내부 트랜잭션 롤백");
    assertThatThrownBy(() -> txManager.rollback(inner)).isInstanceOf(IllegalStateException.class);
  }
}
