package itcenapi.todoapi.repository;

import itcenapi.todoapi.entity.TodoEntity;
import itcenapi.userapi.entity.UserEntity;
import itcenapi.userapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Commit // 테스트 실행 후 커밋. insert select 순서 오류 같은거 잡아줄 수 있음
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    UserRepository userRepository;

    /*@BeforeEach
    void insertDummy() {
        TodoEntity todo1 = TodoEntity.builder().title("배운 거 정리하기").build();
        TodoEntity todo2 = TodoEntity.builder().title("직접 해보기").build();
        TodoEntity todo3 = TodoEntity.builder().title("응용 해보기").done(true).build();

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);
    }
*/
    @Test
    @DisplayName("전체조회 테스트")
    void findAllTest() {
        //given
        // 3 dummies

        //when
        List<TodoEntity> alltodo = todoRepository.findAll();

        //then
        assertEquals(3, alltodo.size());
    }

    @Test
    @DisplayName("not Done 목록 조회 테스트")
    void notdoneTest() {

        List<TodoEntity> notDoneTodos = todoRepository.findNotDoneTodos();

        assertEquals(2, notDoneTodos.size());
    }

    @Test
    @DisplayName("회원의 할 일을 등록해야 한다.")
    void saveTodoWithUserTest() {
        UserEntity user = userRepository.findByEmail("backcoder2@gmail.com");

        // given
        TodoEntity todo = TodoEntity.builder()
                .title("두번째 할일")
                .user(user)
                .build();
        // when
        TodoEntity savedTodo = todoRepository.save(todo);
        // then
        assertEquals(savedTodo.getUser().getId(), user.getId());
    }


    @Test
    @DisplayName("특정 회원 할일 목록 조회")
    @Transactional
    void findByUserTest() {
        //given
        String userId = "402880ab85ed027c0185ed1fc7ea0004";

        //when
        List<TodoEntity> todos = todoRepository.findByUser(userId);

        //then
        todos.forEach(System.out::println);
        assertEquals(2, todos.size());
    }
}