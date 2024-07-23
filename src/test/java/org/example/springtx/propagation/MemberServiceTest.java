package org.example.springtx.propagation;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  LogRepository logRepository;

  /**
   * MemberService    @Transactional: OFF
   * MemberRepository @Transactional: ON
   * LogRepository    @Transactional: ON
   */
  @Test
  void outerTxOff_success() {
    //given
    String username = "outerTxOff_success";

    //when
    memberService.joinV1(username);

    //then
    assertThat(memberRepository.findByUsername(username)).isNotEmpty();
    assertThat(logRepository.findMessage(username)).isNotEmpty();
  }
}
