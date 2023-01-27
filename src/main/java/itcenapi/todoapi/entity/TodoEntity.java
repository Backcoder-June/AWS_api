package itcenapi.todoapi.entity;

import itcenapi.userapi.entity.UserEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "todoId")
@Builder
@Entity
@Table(name = "todo")
public class TodoEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String todoId;

    @Column(nullable = false, length = 30)
    private String title;
    private boolean done; //기본값 false 니까 insert 할 때 안넣어줘도 자동 false
    @CreationTimestamp
    private LocalDateTime createdDate;

    //회원과 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    //우회용 외래키 => 애가 joinColumn 에서 객체 대신 String 으로 차지
    @Column(name = "user_id")
    private String userId;
}