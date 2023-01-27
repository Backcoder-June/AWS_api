package itcenapi.todoapi.service;

import itcenapi.todoapi.dto.request.TodoCreateRequestDTO;
import itcenapi.todoapi.dto.request.TodoModifyRequestDTO;
import itcenapi.todoapi.dto.response.TodoListResponseDTO;
import itcenapi.todoapi.dto.response.TodoReponseDTO;
import itcenapi.todoapi.entity.TodoEntity;
import itcenapi.todoapi.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TodoService {
    private final TodoRepository todoRepository;
    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    // 전체 조회
    @Transactional
    public TodoListResponseDTO retrieve(String userId) {
        List<TodoEntity> entityList = todoRepository.findByUser(userId);

        // entityList => ResponseDTO List
        List<TodoReponseDTO> dtoList = entityList.stream()
                .map(te -> new TodoReponseDTO(te))
                .collect(Collectors.toList());

        return TodoListResponseDTO.builder()
                        .todos(dtoList)
                                .build();
    }

    // 등록
    public TodoListResponseDTO create(final TodoCreateRequestDTO createRequestDTO, final String userId) throws RuntimeException {

        // JPA 의 괴랄한 한계때문에 => Join 할때 userEntity 는 객체 자체를 가지오 와서 그걸 Set 해주는게 개빡이다.
        // 여기서 직접 찾아서 Set하고 해줘야함 => 불합리 => 이걸 해결하기위해 @joinColumn 에 insertable=false, updateable =false 하고
        // privat String userId; 라는 수정시 사용할 외래키를 따로 추가해서 사용하는 방법으로 우회한다.
        TodoEntity todo = createRequestDTO.toEntity();
        // 우회해둔 userId만 security 꺼 가져와서 Set (Id만 set 할 수 있게 됨)
        todo.setUserId(userId);
        todoRepository.save(todo);

        log.info("todo save 성공 - {}", createRequestDTO.getTitle());
        return retrieve(userId); // 이걸 리턴하면 갱신된 리스트를 새로고침 해서 가져다 주는 효과, 즉 추가된 거 포함해서 전체 리스트를 리턴
    }

    // 수정
    public TodoListResponseDTO update(final String todoId, final String userId, final TodoModifyRequestDTO todoModifyRequestDTO) throws IllegalArgumentException {
        System.out.println("====================== \n" + todoId);
        Optional<TodoEntity> targetEntity = todoRepository.findById(todoId);

        targetEntity.ifPresent(entity -> {
            entity.setTitle(todoModifyRequestDTO.getTitle());
            entity.setDone(todoModifyRequestDTO.isDone());

            todoRepository.save(entity); //저장까지 해줘야 완성
        });
        return retrieve(userId);  // 전체목록 조회를 리턴
    }

    // 삭제
    public TodoListResponseDTO delete(final String todoId, final String userId) {

        try {
            todoRepository.deleteById(todoId);
        } catch (Exception e) {
            log.error("{} 번 게시물 삭제에 실패하였습니다. - {}", todoId, e.getMessage()); // 서버 로그용 기록
            throw new RuntimeException("삭제 실패!" + e.getMessage()); //클라에게 던질 기록
        }
        return retrieve(userId);
    }








}
