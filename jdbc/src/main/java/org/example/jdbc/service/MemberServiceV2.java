package org.example.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jdbc.domain.Member;
import org.example.jdbc.repository.MemberRepositoryV2;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

  private final DataSource dataSource;
  private final MemberRepositoryV2 memberRepository;

  public void accountTransfer(String fromId, String toId, int money) throws SQLException {
    Connection con = dataSource.getConnection();

    try {
      con.setAutoCommit(false); //트랜잭션 시작
      //비즈니스 로직
      bizLogic(con, fromId, toId, money);
      con.commit(); //성공 -> 커밋
    } catch (Exception e) {
      con.rollback(); //실패 -> 롤백
      throw new IllegalStateException(e);
    } finally {
      if (con != null) {
        releaseConnection(con);
      }
    }
  }

  private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
    Member fromMember = memberRepository.findById(con, fromId);
    Member toMember = memberRepository.findById(con, toId);

    memberRepository.update(con, fromId, fromMember.getMoney() - money);
    validation(toMember);
    memberRepository.update(con, toId, toMember.getMoney() + money);
  }

  private static void releaseConnection(Connection con) {
    try {
      con.setAutoCommit(true); //커넥션 풀 고려
      con.close();
    } catch (Exception e) {
      log.info("error", e);
    }
  }

  private static void validation(Member toMember) {
    if (toMember.getMemberId().equals("ex")) {
      throw new IllegalStateException("이체 중 예외 발생");
    }
  }
}
