package org.example.jdbc.repository;

import java.sql.SQLException;
import org.example.jdbc.domain.Member;

public interface MemberRepository {

  Member save(Member member);

  Member findById(String memberId);

  void update(String memberId, int money);

  void delete(String memberId);
}
