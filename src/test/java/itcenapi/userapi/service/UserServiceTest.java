package itcenapi.userapi.service;

import itcenapi.userapi.dto.LoginResponseDTO;
import itcenapi.userapi.dto.UserSignUpDTO;
import itcenapi.userapi.dto.UserSignUpResponseDTO;
import itcenapi.userapi.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    @DisplayName("중복 이메일 회원가입 시, RuntimeException 발생")
    void validateEmailTestFail() {
        //given
        UserSignUpDTO tom = UserSignUpDTO.builder()
                .email("backcoder@def.com")
                .password("1111")
                .userName("Tom")
                .build();

        //when
        //then
        // exception 처리에 대한 Test 는 => assertThrows( exception.class, () -> {} ); 활용
        // when then 동시에 처리
        assertThrows(RuntimeException.class, () -> {
            UserSignUpResponseDTO join = userService.join(tom);
        });
    }


    @Test
    @DisplayName("정상 이메일 회원가입 시 성공")
    void validateEmailTestSuccess() {
        //given
        UserSignUpDTO tom = UserSignUpDTO.builder()
                .email("encodedcoder2@def.com")
                .password("2222")
                .userName("encoder2")
                .build();

        //when
        UserSignUpResponseDTO joinedUser = userService.join(tom);

        //then
        assertEquals("encoder2", joinedUser.getUserName());
    }

    @Test
    @DisplayName("존재하지 않는 이메일 로그인 시도 exception 발생")
    void notRegisterdMemberTest() {
        //given
        String email = "tester122112@gmail.com";
        String password = "qlalfqjsgh12!@";

        //when
//        UserEntity loginUser = userService.getByCredentials(email, password);

        //then
//        assertNull(loginUser);
        assertThrows(RuntimeException.class, () -> {
            userService.getByCredentials(email, password);
        });
    }

    @Test
    @DisplayName("비밀번호 불일치 시 exception 발생")
    void worngPasswordTest() {
        //given
        String email = "tester@gmail.com";
        String password = "123411";

        //when then
        assertThrows(RuntimeException.class, () -> {
            userService.getByCredentials(email, password);
        });

    }


    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() {
        //given
        String email = "tester@gmail.com";
        String password = "qlalfqjsgh12!@";

        //when
        LoginResponseDTO responseDTO = userService.getByCredentials(email, password);

        //then
        assertEquals("테스터", responseDTO.getUserName());
    }

//
}