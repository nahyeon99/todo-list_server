package todo.todoList.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.todoList.domain.ToDo;
import todo.todoList.repository.ToDoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ToDoService {

    // 생성자 주입 어노테이션 사용
    private final ToDoRepository toDoRepository;

    /**
     * 투두 추가
     */
    @Transactional
    public Long join(ToDo todo) {
        toDoRepository.save(todo);
        return todo.getId();
    }

    /**
     * 투두 수정
     */
    @Transactional
    public void update(Long id, Boolean isCompleted) {
        ToDo toDo = toDoRepository.findOne(id);
        toDo.setIsCompleted(isCompleted);
        toDo.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * 투두 삭제
     */
    @Transactional
    public void delete(Long todoId) {
        toDoRepository.deleteToDo(todoId);
    }

    /**
     * 투두 조회
     */
    // 같은 기능을 제공하는데 굳이 서비스 계층과 레포지토리 계층을 나누는 이유는 무엇일까..?
    public ToDo findOne(Long todoId) {
        return toDoRepository.findOne(todoId);
    }

    public List<ToDo> findAllWithMemberToDo(Long id) {
        return toDoRepository.findAllWithMemberToDo();
    }

    public ToDo findMemberById(Long id) {
        return toDoRepository.findMemberById(id);
    }

}
