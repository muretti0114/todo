package jp.kobespiral.todo.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jp.kobespiral.todo.exception.ToDoException;

/**
 * ToDoコントローラの例外ハンドラ
 */
@ControllerAdvice
public class ToDoControllerAdvice {

    /**
     * ToDo ビジネス例外のハンドリング
     * @param ex
     * @param model
     * @return カスタム・エラーページを表示
     */
    @ExceptionHandler(ToDoException.class)
    public String handleToDoException(ToDoException ex, Model model) {
        model.addAttribute("error", ex);
        return "todoerror";
    }

    /**
     * Spring バリデーション例外のハンドリング
     * @param ex
     * @param model
     * @return カスタム・エラーページを表示
     */
    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, Model model) {
        //ビジネス例外でラップして，上のハンドラに投げる
        ToDoException e = new ToDoException(ToDoException.INVALID_FORM_DATA, ex.getMessage());
        return handleToDoException(e, model);
    }

    /**
     * その他，一般例外のハンドリング
     * @param ex
     * @param model
     * @return カスタム・エラーページを表示
     */
    @ExceptionHandler(Exception.class)
    public String handleBindException(Exception ex, Model model) {
        //ビジネス例外でラップして，上のハンドラに投げる
        ToDoException e = new ToDoException(ToDoException.ERROR, ex.getMessage());
        return handleToDoException(e, model);
    }


}