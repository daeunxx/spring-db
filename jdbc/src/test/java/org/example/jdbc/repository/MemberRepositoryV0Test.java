package org.example.jdbc.repository;

import static org.assertj.core.api.Assertions.*;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.example.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

@Slf4j
class MemberRepositoryV0Test {

  MemberRepositoryV0 repository = new MemberRepositoryV0();

  @Test
  void crud() throws SQLException {

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
    assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(
        NoSuchElementException.class);
  }
}