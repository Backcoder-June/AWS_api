package itcenapi.userapi.service;

import itcenapi.userapi.dto.UserSignUpDTO;
import itcenapi.userapi.dto.UserSignUpResponseDTO;
import itcenapi.userapi.entity.UserEntity;
import itcenapi.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
//@RequiredArgsConstructor  //=> final 걸린 애들 생성자 자동 만듬 => Autowired 까지 자동 ( 버전 이후 문법 )
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 처리
    public UserSignUpResponseDTO join(final UserSignUpDTO userSignUpDTO) {
        if (userSignUpDTO == null) {
            // exception 종류가 다 runtime 이면 catch 할 때 구분할 수없다. => 사용자 정의 exception 만들어서 사용
            throw new RuntimeException("가입할 회원 정보가 없습니다.");
        }

        final String email = userSignUpDTO.getEmail();

        if(userRepository.existsByEmail(email)) {
            log.warn("Email already exists - {}", email);
            throw new RuntimeException("중복된 이메일");
        }

        // 패스워드 인코딩
        String rawPassword = userSignUpDTO.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        userSignUpDTO.setPassword(encodedPassword);

        UserEntity savedUser = userRepository.save(userSignUpDTO.toEntity());

        log.info("회원가입 성공 - user_id : {}", savedUser.getId());

        // responseDTO 로 바꿔서 응답
        return new UserSignUpResponseDTO(savedUser);
    }
}






