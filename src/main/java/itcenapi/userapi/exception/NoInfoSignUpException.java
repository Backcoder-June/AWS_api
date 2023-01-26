package itcenapi.userapi.exception;

// 사용자 정의 예외 클래스 => RuntimeException 상속
// 1. 기본생성자 2. 에러메세지 처리 생성자 규칙
// 예외상황 1. dto 가 null 2. 이메일 중복
// => 멀티 catch 로 처리
public class NoInfoSignUpException extends RuntimeException{

    // 기본생성자
    public NoInfoSignUpException() {}

    // 에러메세지 처리 생성자
    public NoInfoSignUpException(String message) {
        super(message);
    }


}
