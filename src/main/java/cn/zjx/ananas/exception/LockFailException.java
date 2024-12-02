package cn.zjx.ananas.exception;

/**
 * 锁失败异常
 *
 * @author 钟家兴
 * @date 2024/12/2 10:41
 */
public class LockFailException extends RuntimeException {

    public LockFailException() {
        super();
    }

    public LockFailException(String message) {
        super(message);
    }

    public LockFailException(String message, Throwable cause) {
        super(message, cause);
    }

}
