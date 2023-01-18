package itcenapi.todoapi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import itcenapi.todoapi.entity.TodoEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
@Builder
public class TodoReponseDTO {

    private String todoId;
    private String title;
    private boolean done;
    @JsonFormat(pattern = "yyyy년 MM월 dd일 a hh시 mm분 ss초")
    private LocalDateTime regDate;
    public TodoReponseDTO(TodoEntity todoEntity) {
        this.todoId = todoEntity.getTodoId();
        this.title = todoEntity.getTitle();
        this.done = todoEntity.isDone();
        this.regDate = todoEntity.getCreatedDate();
    }


}
