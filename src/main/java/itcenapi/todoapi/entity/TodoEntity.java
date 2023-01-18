package itcenapi.todoapi.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter @Getter
@AllArgsConstructor @NoArgsConstructor
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


}
