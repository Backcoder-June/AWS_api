package itcenapi.userapi.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class UserApiController {
    private final UserService userService;
    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value ="/signup", method = RequestMethod.POST)
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


//
}
