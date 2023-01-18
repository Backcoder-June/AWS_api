package itcenapi.todoapi.dto.response;

import lombok.*;

import java.util.List;

@Setter @Getter @Builder
@NoArgsConstructor @AllArgsConstructor
public class TodoListResponseDTO {

    private String error;
    private List<TodoReponseDTO> todos;

}
