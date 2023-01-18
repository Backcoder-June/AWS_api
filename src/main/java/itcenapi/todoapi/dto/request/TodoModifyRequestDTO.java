package itcenapi.todoapi.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Builder @ToString
public class TodoModifyRequestDTO {

    @NotBlank
    @Size(min = 1, max = 10)
    private String title;
    private boolean done;
}
