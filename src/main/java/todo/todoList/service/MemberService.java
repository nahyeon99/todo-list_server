package todo.todoList.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.todoList.domain.Member;
import todo.todoList.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service // ComponentScan의 대상이 되서 스프링빈에 자동 등록
@Transactional(readOnly = true) // 데이터 변경해야할 때 필요, readOnly 시 JPA가 성능을 더 최적화시킬 수 있다.
@RequiredArgsConstructor
public class MemberService {

    // @Autowired로 필드 Injection 해줄 수 있지만, 접근 그리고 테스트 시 변경도 어렵다.
    // 그래서 생성자 injection을 많이 사용한다. @RequiredArgsConstructor 혹은 @AllArgsConstructor
    private  final MemberRepository memberRepository; // final로 선언 시 파라미터 입력 안했을 때 컴파일 오류로 체크할 수 있다.

    /**
     * 회원 가입
     */
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByEmail(member.getEmail());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * 회원 정보 수정
     */
    @Transactional
    public void update(Long id, int age, String name) {
        Member member = memberRepository.findOne(id);
        member.setAge(age);
        member.setName(name);
        member.setUpdatedAt(LocalDateTime.now());
    }
}
