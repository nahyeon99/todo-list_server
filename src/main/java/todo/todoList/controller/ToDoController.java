package todo.todoList.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import todo.todoList.domain.ToDo;
import todo.todoList.service.MemberService;
import todo.todoList.service.ToDoService;

import javax.validation.Valid;
import java.time.LocalDateTime;

// @Controller + @ResponseBody // Rest API 스타일
// @ResponseBody 데이터 자체를 json 이나 xml로 보내자
// @ResponseBody http 요청 body를 자바 객체로 전달받을 수 있다.
@RestController
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;
    private final MemberService memberService;


    /**
     * todo 생성 기능
     */
    @PostMapping("/members/{memberId}/todos")
    public CreateToDoResponse createToDo(
            @PathVariable("memberId") Long memberId, @RequestBody @Valid CreateToDoRequest request) {
        ToDo toDo = ToDo.createToDo(memberService.findOne(memberId), request.content);
        Long id = toDoService.join(toDo);
        return new CreateToDoResponse(id);
    }

    /**
     * todo 수정 기능 - 완료 여부만 변경
     */
    @PatchMapping("/todos/{todoid}")
    public UpdateToDoResponse updateToDoResponse(
            @PathVariable("todoid") Long id,
            @RequestBody @Valid UpdateToDoRequest request
    ) {
        toDoService.update(id, request.getIsCompleted());
        ToDo findToDo = toDoService.findOne(id);
        return new UpdateToDoResponse(findToDo.getId(), findToDo.getIsCompleted());
    }

    /**
     * todo 삭제 기능
     */
    @DeleteMapping("/todos/{todoid}")
    public DeleteToDoResponse deleteToDoResponse(
            @PathVariable("todoid") Long id
    ) {
        toDoService.delete(id);
        return new DeleteToDoResponse(id);
    }

    /**
     * todo 를 식별자를 이용하여 조회 기능 (회원까지 같이 조회 다대일)
     */
    @GetMapping("/todos/{todoid}")
    public GetToDoResponse getToDoResponse(
            @PathVariable("todoid") Long id
    ) {
        ToDo findToDo = toDoService.findOne(id);
        return new GetToDoResponse(findToDo);
    }

    /**
     * DTO
     */
    @Data
    static class GetToDoResponse {
        private String name;
        private String content;
        private Long todoId;
        private Long memberId;
        private Boolean isCompleted;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public GetToDoResponse(ToDo todo) {
            content = todo.getContent();
            todoId=todo.getId();
            memberId=todo.getMember().getId();
            name=todo.getMember().getName();
            isCompleted=todo.getIsCompleted();
            createdAt = todo.getCreatedAt();
            updatedAt=todo.getUpdatedAt();
        }
    }

    /**
     * TODO 생성 - 응답 : 상태코드만
     */

    @Data
    static class CreateToDoRequest {
        private Long memberId;
        private String content;
    }

    @Data
    static class CreateToDoResponse {
        private Long id;

        public CreateToDoResponse(Long id) {
            this.id = id;
        }
    }

    /**
     * TODO 수정 - 응답 : 상태코드만
     */
    @Data
    @AllArgsConstructor
    static class UpdateToDoRequest {
        private Boolean isCompleted;
        public UpdateToDoRequest() {

        }
    }

    /**
     * TODO 수정 - 응답 : 상태코드만
     */
    @Data
    @AllArgsConstructor
    static class UpdateToDoResponse {
        private Long id;
        private Boolean isCompleted;
    }

    /**
     * TODO 삭제 - 응답 : 상태코드만
     */
    @Data
    @AllArgsConstructor
    static class DeleteToDoResponse {
        private Long id;
    }

    @Data
    static class ToDoDto {
        private Long toDoId;
        private String name;
        private String content;
        private Boolean isCompleted;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long memberId;

        public ToDoDto(ToDo toDo) {
            toDoId = toDo.getId();
            name = toDo.getMember().getName();
            content = toDo.getContent();
            createdAt = toDo.getCreatedAt();
            isCompleted = toDo.getIsCompleted();
            memberId = toDo.getMember().getId();
        }


    }

}
