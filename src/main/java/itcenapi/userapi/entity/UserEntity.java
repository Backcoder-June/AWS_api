package itcenapi.userapi.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@EqualsAndHashCode(of = "id") //id로만 비교해라
@ToString
@NoArgsConstructor @AllArgsConstructor
@Entity @Builder
@Table(name = "tbl_user")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String userName;
    @CreationTimestamp
    private LocalDateTime joinDate;
}
