package jp.kobespiral.todo.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ToDoDto {
    Long tid;
    String title;
    String description;
    boolean isOpen;

    Date createdAt;
    Date updateAt;
    Date doneAt;
    String uid;
    String name;
}