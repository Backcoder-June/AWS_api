package itcenapi.userapi.repository;

import itcenapi.userapi.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공 테스트")
    @Transactional //테스트마다 초기화
    @Rollback
    void joinTest() {
        //given
        UserEntity newUser = UserEntity.builder()
                .email("backcoder@def.com")
                .password("1234")
                .userName("June")
                .build();

        //when
        UserEntity savedUser = userRepository.save(newUser);

        //then
        assertNotNull(savedUser);
    }

    @Test
    @DisplayName("이메일로 회원 조회")
    void findByEmailTest() {
        //given
        String email = "backcoder@def.com";

        //when
        UserEntity foundUser = userRepository.findByEmail(email);

        //then
        assertEquals("June", foundUser.getUserName());
    }


    @Test
    @DisplayName("이메일 중복을 체크")
    void existsEmailTest() {
        //given
        String email = "backcoder@def.com";

        //when
        boolean flag = userRepository.existsByEmail(email);

        //then
        assertTrue(flag);
    }
}