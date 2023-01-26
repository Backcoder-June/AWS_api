package itcenapi.userapi.exception;

public class DuplicatedEmailException extends RuntimeException {

    // 기본생성자
    public DuplicatedEmailException() {}

    // 에러메세지 처리 생성자
    public DuplicatedEmailException(String message) {
        super(message); //runTimeException 에서 받아옴
    }

}
