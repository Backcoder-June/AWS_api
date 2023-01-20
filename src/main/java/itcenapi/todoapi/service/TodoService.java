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
    public TodoListResponseDTO retrieve() {
        List<TodoEntity> entityList = todoRepository.findAll();

        // entityList => ResponseDTO List
        List<TodoReponseDTO> dtoList = entityList.stream()
                .map(te -> new TodoReponseDTO(te))
                .collect(Collectors.toList());

        return TodoListResponseDTO.builder()
                        .todos(dtoList)
                                .build();
    }

    // 등록
    public TodoListResponseDTO create(final TodoCreateRequestDTO createRequestDTO) throws RuntimeException {
        todoRepository.save(createRequestDTO.toEntity());
        log.info("todo save 성공 - {}", createRequestDTO.getTitle());
        return retrieve(); // 이걸 리턴하면 갱신된 리스트를 새로고침 해서 가져다 주는 효과, 즉 추가된 거 포함해서 전체 리스트를 리턴
    }

    // 수정
    public TodoListResponseDTO update(final String todoId, final TodoModifyRequestDTO todoModifyRequestDTO) throws IllegalArgumentException {
        Optional<TodoEntity> targetEntity = todoRepository.findById(todoId);

        targetEntity.ifPresent(entity -> {
            entity.setTitle(todoModifyRequestDTO.getTitle());
            entity.setDone(todoModifyRequestDTO.isDone());

            todoRepository.save(entity); //저장까지 해줘야 완성
        } );
        return retrieve();  // 전체목록 조회를 리턴
    }

    // 삭제
    public TodoListResponseDTO delete(final String todoId) {

        try {
            todoRepository.deleteById(todoId);
        } catch (Exception e) {
            log.error("{} 번 게시물 삭제에 실패하였습니다. - {}", todoId, e.getMessage()); // 서버 로그용 기록
            throw new RuntimeException("삭제 실패!" + e.getMessage()); //클라에게 던질 기록
        }
        return retrieve();
    }








}
