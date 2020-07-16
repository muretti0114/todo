package jp.kobespiral.todo.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import jp.kobespiral.todo.entity.ToDo;
import lombok.Data;

@Data
public class ToDoForm {
    @NotBlank
    String title;

    String description;

    String uid;

    public ToDo toEntity() {
        ToDo t = new ToDo(null, title, description, true, new Date(), new Date(), null, uid);
        return t;
    }

}