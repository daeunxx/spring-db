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

  /**
   * MemberService    @Transactional: OFF
   * MemberRepository @Transactional: ON
   * LogRepository    @Transactional: ON Exception
   */
  @Test
  void outerTxOff_fail() {
    //given
    String username = "로그예외_outerTxOff_fail";

    //when
    assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

    //then
    System.out.println("++++++" + memberRepository.findByUsername(username));
    assertThat(memberRepository.findByUsername(username)).isNotEmpty();
    assertThat(logRepository.findMessage(username)).isEmpty();
  }

  /**
   * MemberService    @Transactional: ON
   * MemberRepository @Transactional: OFF
   * LogRepository    @Transactional: OFF
   */
  @Test
  void singleTx() {
    //given
    String username = "singleTx";

    //when
    memberService.joinV1(username);

    //then
    assertThat(memberRepository.findByUsername(username)).isNotEmpty();
    assertThat(logRepository.findMessage(username)).isNotEmpty();
  }

  /**
   * MemberService    @Transactional: ON
   * MemberRepository @Transactional: ON
   * LogRepository    @Transactional: ON
   */
  @Test
  void outerTxOn_success() {
    //given
    String username = "outerTxOn_success";

    //when
    memberService.joinV1(username);

    //then: 모든 데이터 정상 저장
    assertThat(memberRepository.findByUsername(username)).isNotEmpty();
    assertThat(logRepository.findMessage(username)).isNotEmpty();
  }
}
