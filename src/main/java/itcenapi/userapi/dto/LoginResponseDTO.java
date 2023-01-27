package itcenapi.userapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import itcenapi.userapi.entity.UserEntity;
import lombok.*;

import java.time.LocalDate;

@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String email;
    private String userName;
    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDate joinDate;

    private String token;

    private String message;

    // Entity => DTO
    public LoginResponseDTO(UserEntity userEntity, String token) { //토큰도 받아야 함
        this.email = userEntity.getEmail();
        this.userName = userEntity.getUserName();
        this.joinDate = LocalDate.from(userEntity.getJoinDate()); //localdateTime => localdate
        this.token = token;
    }
}
