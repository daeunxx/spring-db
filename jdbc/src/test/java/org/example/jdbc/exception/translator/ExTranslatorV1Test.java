package org.example.jdbc.exception.translator;

import static org.example.jdbc.connection.ConnectionConst.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jdbc.domain.Member;
import org.example.jdbc.repository.ex.MyDbException;
import org.example.jdbc.repository.ex.MyDuplicateKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
public class ExTranslatorV1Test {

  Service service;
  Repository repository;

  @BeforeEach
  void init() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    repository = new Repository(dataSource);
    service = new Service(repository);
  }

  @Test
  void duplicateKeySave() {
    service.create("myId");
    service.create("myId"); //같은 ID 저장 시도
  }

  @Slf4j
  @RequiredArgsConstructor
  static class Service {

    private final Repository repository;

    public void create(String memberId) {
      try {
        repository.save(new Member(memberId, 0));
        log.info("saveId={}", memberId);
      } catch (MyDuplicateKeyException e) {
        log.info("키 중복, 복구 시도");
        String retryId = generateNewId(memberId);
        log.info("retryId={}", retryId);
        repository.save(new Member(retryId, 0));
      } catch (MyDbException e) {
        log.info("데이터 접근 제어 계층 예외", e);
        throw e;
      }
    }

    private String generateNewId(String memberId) {
      return memberId + new Random().nextInt(1000);
    }
  }

  @RequiredArgsConstructor
  static class Repository {

    private final DataSource dataSource;

    public Member save(Member member) {
      String sql = "insert into member(member_id, money) values(?, ?)";
      Connection con = null;
      PreparedStatement pstmt = null;

      try {
        con = dataSource.getConnection();
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, member.getMemberId());
        pstmt.setInt(2, member.getMoney());
        pstmt.executeUpdate();
        return member;
      } catch (SQLException e) {
        //h2 DB
        if (e.getErrorCode() == 23505) {
          throw new MyDuplicateKeyException(e);
        }
        throw new MyDbException(e);
      } finally {

      }
    }
  }
}
