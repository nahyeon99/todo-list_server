package todo.todoList.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import todo.todoList.domain.Member;
import todo.todoList.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class) // junit 실행할 때 스프링 엮어서 실행
@SpringBootTest // 스프링 부트를 띄운 상태에서 테스트를 하기 위함
@Transactional // 데이터 변경을 위한, rollback이 default
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("나현킴");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setEmail("blueberry@inu.ac.kr");

        Member member2 = new Member();
        member2.setEmail("blueberry@inu.ac.kr");

        //when
        memberService.join(member1);
        memberService.join(member2); // 같은 이메일로 가입했으므로 예외가 발생해야 한다.

        //then
        Assert.fail("예외가 발생해야 한다.");
    }

}
