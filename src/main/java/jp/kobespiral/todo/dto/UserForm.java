package jp.kobespiral.todo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * ユーザ登録用のフォーム
 */
@Data
public class UserForm {
    @Size(max = 16)
    @Pattern(regexp = "[a-z0-9_\\-]+")
    String uid;  //ユーザID

    @NotBlank
    @Size(max = 64)
    String name; //名前

}