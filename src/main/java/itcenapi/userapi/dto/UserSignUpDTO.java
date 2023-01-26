package itcenapi.userapi.dto;

import itcenapi.userapi.entity.UserEntity;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// 회원 가입 시 클라이언트 input 담는 객체
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder @EqualsAndHashCode(of ="email")
public class UserSignUpDTO {

    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
    @NotBlank
    @Size(min=2, max=6)
    private String userName;


    // toEntity 메소드
    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(this.email)
                .password(this.password)
                .userName(this.userName)
                .build();
    }
}
