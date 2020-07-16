package jp.kobespiral.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * ToDoのフォーム．タイトルと詳細説明のみ．その他の属性はサービス層で付与される
 */
@Data
public class ToDoForm {
    @NotBlank
    @Size(max=32)
    String title; //タイトル

    @Size(max=512)
    String description; //詳細説明

}