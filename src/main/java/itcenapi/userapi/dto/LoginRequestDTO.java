package itcenapi.userapi.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@EqualsAndHashCode
public class LoginRequestDTO {
    @Email @NotBlank
    private String email;

    @NotBlank @Size(min=8, max=20)
    private String password;


}
