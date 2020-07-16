package jp.kobespiral.todo.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jp.kobespiral.todo.exception.ToDoException;

@ControllerAdvice
public class ToDoControllerAdvice {

    @ExceptionHandler(ToDoException.class)
    public String handleToDoException(ToDoException ex, Model model) {
        model.addAttribute("error", ex);
        return "todo_error";
    }
    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException ex, Model model) {
        ToDoException e = new ToDoException(ToDoException.INVALID_USER_ID, ex.getMessage());
        return handleToDoException(e, model);
    }


}