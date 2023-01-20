package itcenapi.todoapi.service;

import itcenapi.todoapi.dto.request.TodoCreateRequestDTO;
import itcenapi.todoapi.dto.request.TodoModifyRequestDTO;
import itcenapi.todoapi.dto.response.TodoListResponseDTO;
import itcenapi.todoapi.dto.response.TodoReponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Commit
class TodoServiceTest {

    @Autowired
    TodoService todoService;

    @BeforeEach
    void insertDummies() {
        TodoCreateRequestDTO dto1 = TodoCreateRequestDTO.builder().title("기록하기").build();
        TodoCreateRequestDTO dto2 = TodoCreateRequestDTO.builder().title("정리하기").build();
        TodoCreateRequestDTO dto3 = TodoCreateRequestDTO.builder().title("응용하기").build();

        todoService.create(dto1);
        todoService.create(dto2);
        todoService.create(dto3);
    }


    @Test
    @DisplayName("insert 테스트 + 전체 리스트 개수 테스트 3더미스 + 1 ")
    void createTest() {
        //given
        //3dummies
        TodoCreateRequestDTO newTodo = TodoCreateRequestDTO.builder()
                .title("새로운일")
                .build();

        //when
        TodoListResponseDTO todoListResponseDTO = todoService.create(newTodo);

        //then
        List<TodoReponseDTO> todos = todoListResponseDTO.getTodos();
        assertEquals(4, todos.size());

        System.out.println("==============");
        for (TodoReponseDTO todo : todos) {
            System.out.println(todo);
        }
    }

    @Test
    @DisplayName("수정테스트 - 제목 하고 done 처리 ")
    void updateTest() {

        //given
        String newTitle = "수정된제목";
        boolean newDone = true;
        TodoModifyRequestDTO modifydto = TodoModifyRequestDTO.builder()
                .title(newTitle)
                .done(newDone)
                .build();

        //when
        TodoReponseDTO targetTodo = todoService.retrieve().getTodos().get(1);

        TodoListResponseDTO updatedTodo = todoService.update(targetTodo.getTodoId(), modifydto);

        //then
        assertEquals("수정된제목", updatedTodo.getTodos().get(1).getTitle());
        assertTrue(updatedTodo.getTodos().get(1).isDone());
        System.out.println("============");



        }
    }
