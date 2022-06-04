package todo.todoList.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import todo.todoList.domain.Member;
import todo.todoList.domain.ToDo;
import todo.todoList.service.MemberService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@ResponseBody
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 생성 기능
     */

    @PostMapping("/members")
    public CreateMemberResponse createMember(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());
        member.setAge(request.getAge());
        member.setEmail(request.getEmail());
        member.setCreatedAt(LocalDateTime.now());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 회원 수정 기능
     */
    @PatchMapping("/members/{id}")
    public UpdateMemberResponse updateMember(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        memberService.update(id, request.getAge(), request.getName());
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName(), findMember.getAge());
    }

    /**
     * 회원 리스트 조회 기능 - 회원만
     */
    @GetMapping("/members")
    public Result findMemberAll() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberToDo> collect = findMembers.stream()
                .map(m -> new MemberToDo(m))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    /**
     * 회원 단건 조회
     */
    @GetMapping("/members/{id}")
    public MemberToDo findMemberOne(@PathVariable("id") Long id) {
        Member member = memberService.findOne(id);
        return new MemberToDo(member);
    }


    /**
     * DTO
     */
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
        @NotEmpty
        private String email;
        private Integer age;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    class UpdateMemberRequest {
        @NotEmpty
        private String name;
        private Integer age;

    }

    @Data
    @AllArgsConstructor
    class UpdateMemberResponse {
        private Long id;
        private String name;
        private Integer age;
    }

    @Data
    @AllArgsConstructor // 이건 왜 사용했을까?
    class MemberToDo {
        private Long id;
        private String name;
        private Integer age;
        private String email;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<TodoListDto> todoList;

        public MemberToDo(Member member) {
            id = member.getId();
            age = member.getAge();
            email = member.getEmail();
            createdAt = member.getCreatedAt();
            updatedAt = member.getUpdatedAt();
            name = member.getName();
            todoList = member.getToDoList().stream()
                    .map(todo -> new TodoListDto(todo))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class TodoListDto{
        private Long todoId;
        private String content;
        private Boolean isCompleted;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;
        public TodoListDto(ToDo todo){
            content=todo.getContent();
            todoId=todo.getId();
            isCompleted=todo.getIsCompleted();
            createAt=todo.getCreatedAt();
            updateAt=todo.getUpdatedAt();
        }
    }
}
