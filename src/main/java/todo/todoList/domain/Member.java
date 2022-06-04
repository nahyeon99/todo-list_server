package todo.todoList.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String email;
    private int age;
    private String name;
    private LocalDateTime createdAt; // 생성날짜
    private LocalDateTime updatedAt; // 수정날짜

    // 컬렉션은 필드에서 초기화하는 것이 안전하다. (null 문제 등)
    // 하이버네이트의 메커니즘 대로 안돌아가기 때문에 가능한 절대 변경하지 않는다.
    // @xToOne의 모든 연관관계는 지연로딩으로 설정.
    @OneToMany(mappedBy = "member", fetch = LAZY)
    private List<ToDo> toDoList = new ArrayList<>();
}
