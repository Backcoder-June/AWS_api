package itcenapi.todoapi.repository;

import itcenapi.todoapi.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

    @Query("select t from TodoEntity t where t.done=0")
    List<TodoEntity> findNotDoneTodos();

//    @Query(value = "select * from todo where user_id =:userId", nativeQuery = true) //userId
    @Query("select t from TodoEntity t where user_id =:userId")
    List<TodoEntity> findByUser(@Param("userId") String userId);
}
