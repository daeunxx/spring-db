package org.example.jdbc.repository;

import java.sql.SQLException;
import org.example.jdbc.domain.Member;

public interface MemberRepositoryEx {

  Member save(Member member) throws SQLException;

  Member findById(String memberId) throws SQLException;

  void update(String memberId, int money);

  void delete(String memberId);
}
