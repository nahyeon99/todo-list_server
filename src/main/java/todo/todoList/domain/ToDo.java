package todo.todoList.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class ToDo {

    @Id @GeneratedValue
    @Column(name="todo_id")
    private Long id;

    private String content; // 내용
    private Boolean isCompleted; // 완료여부
    private LocalDateTime createdAt; // 생성날짜
    private LocalDateTime updatedAt; // 수정날짜

    @ManyToOne(fetch = FetchType.LAZY) // ToDo 입장에서 다:1
    @JoinColumn(name = "member_id") // FK 설정, 다 쪽에 연관관계의 주인으로 설정
    private Member member;

    //==연관관계 메서드 ==//
    // 원자적으로 양뱡향 setting
    public void setMember(Member member) {
        this.member = member;
        member.getToDoList().add(this);
    }

    //==생성 메서드==//
    public static ToDo createToDo(Member member, String content) {
        ToDo todo = new ToDo();
        todo.setMember(member);
        todo.setContent(content);
        todo.setIsCompleted(false);
        todo.setCreatedAt(LocalDateTime.now());

        return todo;
    }
}
