package itcenapi.userapi.repository;

import itcenapi.userapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    // 이메일 조회
    // email 조회 시, entity 하나만 조회되야 하므로, unique=true 걸어주자
    UserEntity findByEmail(String email);

    // jpa문 existsBy => select count(*) from tbl_user where email=?
    // jpql : select count(*) from UserEntity u where u.email=:email
    // count 로 받아서 count 있으면 1 true(unique) / 없으면 0 false => boolean

    boolean existsByEmail(String email);





}
