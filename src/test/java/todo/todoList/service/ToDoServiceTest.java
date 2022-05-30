package todo.todoList.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import todo.todoList.domain.ToDo;
import todo.todoList.repository.ToDoRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ToDoServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ToDoService toDoService;
    @Autowired
    ToDoRepository toDoRepository;
    @Autowired
    MemberService memberService;

    // TODO: Test err: InvalidDataAccessApiUsageException
    @Test
    public void 투두_등록() throws Exception {
        //given
        ToDo todo = new ToDo();

        todo.setId(1L);
        todo.setContent("코딩");
        todo.setIsCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());

        //when
        Long todoId = toDoService.join(todo);

        //then
        assertEquals(todo, toDoRepository.findOne(todoId));
    }
}
