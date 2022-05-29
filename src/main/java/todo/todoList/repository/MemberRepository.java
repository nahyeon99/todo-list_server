package todo.todoList.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import todo.todoList.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

// @Component로 ComponentScan의 대상이 되서 스프링빈으로 자동 등록된다.
@Repository
// final로 선언된 필드 한해서 생성자를 만들어준다.
@RequiredArgsConstructor
public class MemberRepository {

    // Spring이 JPA의 EntityManager를 주입해준다.
    private final EntityManager em;

    /**
     * 회원 저장
     */
    public void save(Member member) {
        // 영속성 컨텍스트에 member 엔티티를 넣는다.
        // 트랜잭션이 커밋되는 시점에 db에 반영된다.
        em.persist(member);
    }

    /**
     * 단건 조회
     */
    public Member findOne(Long id) {
        return em.find(Member.class, id); // (타입, PK)
    }

    /**
     * 전체 조회
     */
    public List<Member> findAll() {
        // JPQL, FROM의 대상이 테이블이 아닌 '엔티티'
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /**
     * 이메일로 조회
     */
    public List<Member> findByEmail(String email) {
        return em.createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email) // parameter 바인딩
                .getResultList();
    }
}
