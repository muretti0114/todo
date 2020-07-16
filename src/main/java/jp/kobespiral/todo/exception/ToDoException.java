package jp.kobespiral.todo.exception;


/**
 * ToDoアプリのビジネス例外
 */
public class ToDoException extends RuntimeException {
    private static final long serialVersionUID = -161631335611992421L;
    public static final int NO_SUCH_USER = 1;
    public static final int NO_SUCH_TODO = 2;
    public static final int USER_ALREADY_EXISTS = 11;
    public static final int TODO_ALREADY_EXISTS = 12;
    public static final int INVALID_USER_ID = 31;
    public static final int INVALID_USER_INFO = 32;
    public static final int INVALID_TODO_INFO = 33;
    public static final int INVALID_UID_OF_TODO = 34;
    public static final int TODO_ALREADY_DONE = 35;
    public static final int TODO_NOT_DONE = 36;
    public static final int OPERATION_NOT_AUTHORIZED = 41;

    public static final int ERROR = 99;

    int code;

    public ToDoException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ToDoException(int code,  String message, Exception cause) {
        super(message, cause);
        this.code = code;
    }
    public int getCode() {
        return code;
    }


}