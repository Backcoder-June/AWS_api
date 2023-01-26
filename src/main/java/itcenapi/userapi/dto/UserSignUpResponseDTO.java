package itcenapi.userapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import itcenapi.userapi.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "email")
public class UserSignUpResponseDTO {

    private String email;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate;


    // entity => ResponseDTO // 메소드로 만들어도 되고, 생성자로 만들어도 되고

    public UserSignUpResponseDTO(UserEntity entity) {
        this.email = entity.getEmail();
        this.userName = entity.getUserName();
        this.joinDate = entity.getJoinDate();
    }


}
