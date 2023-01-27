package itcenapi.userapi.controller;

import itcenapi.userapi.dto.LoginRequestDTO;
import itcenapi.userapi.dto.LoginResponseDTO;
import itcenapi.userapi.dto.UserSignUpDTO;
import itcenapi.userapi.dto.UserSignUpResponseDTO;
import itcenapi.userapi.exception.DuplicatedEmailException;
import itcenapi.userapi.exception.NoInfoSignUpException;
import itcenapi.userapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@CrossOrigin
public class UserApiController {
    private final UserService userService;

    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }


    //로그인 요청 처리
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Validated @RequestBody LoginRequestDTO requestDTO) {

        try {
            LoginResponseDTO userInfo = userService.getByCredentials(
                    requestDTO.getEmail(),
                    requestDTO.getPassword()
            );
            return ResponseEntity
                    .ok().body(userInfo);

        } catch (RuntimeException e) {// 오류 생겼을 때는, ResponseDTO 에 오류 메세지만 담아서 보내기
            return ResponseEntity
                    .badRequest()
                    .body(LoginResponseDTO.builder()
                            .message(e.getMessage()) //오류메세지 response 에 빌드해서 넘김
                            .build());
        }
    }



    // 로그인 처리
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@Validated @RequestBody UserSignUpDTO userSignUpDTO, BindingResult bindingResult) {
        log.info("/api/auth/sighup POST! - {}", userSignUpDTO);

        if (bindingResult.hasErrors()) {
            log.warn(bindingResult.toString()); //서버용
            return ResponseEntity
                    .badRequest()
                    .body(bindingResult.toString()); //응답용
        }


        try {
            UserSignUpResponseDTO responseDTO = userService.join(userSignUpDTO);
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
            // 예외상황 1. dto 가 null 2. 이메일 중복
            // => 멀티 catch 로 처리
        } catch (NoInfoSignUpException e) {
            log.warn("필수정보 입력하세요");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (DuplicatedEmailException e) {
            log.warn("중복 이메일");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // 이메일 중복확인 => t /f 리턴
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        if (email == null || email.trim().equals("")) {
            return ResponseEntity.badRequest().body("이메일이 빈값입니다.");
        }
        boolean flag = userService.isDuplicatedEmail(email);
        log.info("{} 중복여부 - {}", email, flag);

        return ResponseEntity.ok().body(flag);
    }


//
}
