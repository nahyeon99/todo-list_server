package todo.todoList.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import todo.todoList.domain.ToDo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ToDoRepository {

    @PersistenceContext
    EntityManager em;

    /**
     * 저장
     */
    public void save(ToDo todo) {
        em.persist(todo);
    }

    /**
     * 단건 조회
     */
    public ToDo findOne(Long id) {
        return em.find(ToDo.class, id); // (type, PK)
    }

    /**
     * 전체 조회
     */
    public List<ToDo> findAllWithMemberToDo() {

        // Fetch Join: 조회의 주체가 되는 엔티티 이외에 연관 엔티티도 함께 select하여 모두 영속화,
        // Lazy인 Entity를 참조하더라도 이미 영속성 컨텍스트에 들어있으므로 N+1 문제 발생하지 않는다.
        // => 한 번에 연관된 엔티티들의 데이터를 조회할 때 Fetch Join 사용

        return em.createQuery("select o from ToDo o" +
                " join fetch o.member m", ToDo.class)
                .getResultList(); // 결과가 하나 이상일 때, 리스트 반환
    }

    /**
     * ToDo id로 'Todo+회원정보' 조회
     */
    public ToDo findMemberById(Long id) {
        return em.createQuery("select o from ToDo o"+
                " join fetch o.member m"+
                " where o.id = :id", ToDo.class)
                .setParameter("id", id)
                .getSingleResult(); // 결과가 정확히 하나, 단일 객체 반환
    }

    /**
     * 삭제
     */
    public void deleteToDo(Long id) {
        em.remove(findOne(id));
    }

}
