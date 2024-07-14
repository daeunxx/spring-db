package org.example.jdbc.repository;

import java.sql.SQLException;
import org.example.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

class MemberRepositoryV0Test {

  MemberRepositoryV0 repository = new MemberRepositoryV0();

  @Test
  void crud() throws SQLException {
    Member member = new Member("memberV0", 10000);
    repository.save(member);
  }
}