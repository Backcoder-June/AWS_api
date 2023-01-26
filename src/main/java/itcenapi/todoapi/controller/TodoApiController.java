package itcenapi.todoapi.controller;

import itcenapi.todoapi.dto.request.TodoCreateRequestDTO;
import itcenapi.todoapi.dto.request.TodoModifyRequestDTO;
import itcenapi.todoapi.dto.response.TodoListResponseDTO;
import itcenapi.todoapi.dto.response.TodoReponseDTO;
import itcenapi.todoapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/todos")  //여기 버전 v1 v2 명시해주기도 하는데, 새로운 버전의 controller 새로 만드는 식 ( 배려 )
//CORS 허용 설정
@CrossOrigin //(origins = {"http://localhost:5501"})
public class TodoApiController {
    private final TodoService todoService;

    @Autowired
    public TodoApiController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 등록
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createTodo(@Validated @RequestBody TodoCreateRequestDTO todoCreateRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("DTO 바인딩 검증 에러 -{}", result.getFieldError());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());
        }
        try {
            TodoListResponseDTO todoListResponseDTO = todoService.create(todoCreateRequestDTO);
            return ResponseEntity
                    .ok()
                    .body(todoListResponseDTO);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(TodoListResponseDTO.builder()
                            .error(e.getMessage()));
        }
    }

    //조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> findTodos() {
        TodoListResponseDTO retrieveDTOs = todoService.retrieve();
        if (retrieveDTOs == null) {
            return ResponseEntity.ok("조회결과가 없습니다.");
        }
        return ResponseEntity.ok()
                .body(retrieveDTOs);
    }

    // 수정
    @RequestMapping(value = "/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<?> updateTodos(@PathVariable("id") String todoId, @Validated @RequestBody TodoModifyRequestDTO todoModifyRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            log.info("수정 바인딩 오류 - {}", result.getFieldError());
            return ResponseEntity.badRequest()
                    .body("수정 내용이 잘못되었습니다." + result.getFieldError());
        }
        try {
            TodoListResponseDTO updatedDTO = todoService.update(todoId, todoModifyRequestDTO);
            return ResponseEntity.ok()
                    .body(updatedDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.internalServerError()
                    .body("서버 에러로 수정에 실패 " + e.getMessage());
        }
    }

    //삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String todoId) {

        log.info("delete requeset id = {}", todoId);

        if (todoId == null || todoId.equals("")) {
            return ResponseEntity
                    .badRequest()
                    .body(TodoListResponseDTO.builder().error("Wrong Id"));
        }
        try {
            TodoListResponseDTO responseDTO = todoService.delete(todoId);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("delete Failed" + e.getMessage());
        }
    }
}
