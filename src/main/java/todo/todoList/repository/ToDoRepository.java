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

    // 저장
    public void save(ToDo todo) {
        em.persist(todo);
    }

    // 단건 조회
    public ToDo findOne(Long id) {
        return em.find(ToDo.class, id); // (type, PK)
    }

    // 전체 조회
    public List<ToDo> findAllWithMemberToDo() {
        return em.createQuery("select o from ToDo o" +
                " join fetch o.member m", ToDo.class)
                .getResultList();
    }

    // ToDo id로 'Todo+회원정보' 조회
    public ToDo findMemberById(Long id) {
        return em.createQuery("select o from ToDo o"+
                " join fetch o.member m"+
                " where o.id = :id", ToDo.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    // 삭제
    public void deleteToDo(Long id) {
        em.remove(findOne(id));
    }

}
