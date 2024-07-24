package org.example.jdbc.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.jdbc.connection.ConnectionConst.*;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.example.jdbc.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class MemberRepositoryV1Test {

  MemberRepositoryV1 repository;

  @BeforeEach
  void beforeEach() {
    //기본 DriverManager: 항상 새로운 커넥션 획득
    //DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

    //커넥션 풀링
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(URL);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    repository = new MemberRepositoryV1(dataSource);
  }

  @Test
  void crud() throws SQLException, InterruptedException {

    //save
    Member member = new Member("memberV2", 10000);
    repository.save(member);

    //findById
    Member findMember = repository.findById(member.getMemberId());
    log.info("findMember={}", findMember);
    log.info("member == findMember {}", member == findMember);
    log.info("member equals findMember {}", member.equals(findMember));
    assertThat(findMember).isEqualTo(member);

    //update money: 10000 -> 20000
    repository.update(member.getMemberId(), 20000);
    Member updatedMember = repository.findById(member.getMemberId());
    assertThat(updatedMember.getMoney()).isEqualTo(20000);

    //repository
    repository.delete(member.getMemberId());
    assertThatThrownBy(() -> repository.findById(member.getMemberId()));

    Thread.sleep(1000);
  }
}