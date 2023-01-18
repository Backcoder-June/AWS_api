package itcenapi.todoapi.dto.request;

import itcenapi.todoapi.entity.TodoEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TodoCreateRequestDTO {

    @NotBlank
    @Size(min = 1, max = 10)
    private String title;

    public TodoEntity toEntity() {
        return TodoEntity.builder()
                .title(this.title)
                .build();
    }

}
