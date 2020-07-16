package jp.kobespiral.todo.dto;

import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import jp.kobespiral.todo.entity.User;
import lombok.Data;

@Data
public class UserForm {
    @Size(max = 16)
    @Pattern(regexp = "[A-z0-9_\\-]+")
    String uid;

    @Size(max = 64)
    String name;
    public User toEntity() {
        User u = new User();
        u.setUid(uid);
        u.setName(name);
        u.setCreatedAt(new Date());
        return u;
    }
}