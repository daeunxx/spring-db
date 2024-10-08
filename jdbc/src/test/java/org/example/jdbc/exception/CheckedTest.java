package org.example.jdbc.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {

  @Test
  void checkedCatch() {
    Service service = new Service();
    service.callCatch();
  }

  @Test
  void checkedThrow() {
    Service service = new Service();
    Assertions.assertThatThrownBy(() -> service.callThrow()).isInstanceOf(MyCheckedException.class);
  }

  //Exception을 상속받은 예외는 체크 예외
  static class MyCheckedException extends Exception {

    public MyCheckedException(String message) {
      super(message);
    }
  }

  /**
   * Checked 예외는 예외를 잡아서 처리 or 던지기
   */
  static class Service {

    Repository repository = new Repository();

    //예외를 잡아서 처리
    public void callCatch() {
      try {
        repository.call();
      } catch (MyCheckedException e) {
        //예외 처리 로직
        log.info("예외 처리, message={}", e.getMessage(), e);
      }
    }

    //예외를 밖으로 던짐
    public void callThrow() throws MyCheckedException {
      repository.call();
    }
  }

  static class Repository {

    public void call() throws MyCheckedException {
      throw new MyCheckedException("ex");
    }
  }
}
