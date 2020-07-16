package jp.kobespiral.todo.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * ToDoのDTO．ユーザ名をジョインしている
 */
@Data
public class ToDoDto {
    Long tid;

    @NotBlank
    @Size(max=32)
    String title;

    @Size(max=512)
    String description;

    boolean isOpen;
    
    Date createdAt;
    Date updatedAt;
    Date doneAt;
    String uid;
    String name;
}