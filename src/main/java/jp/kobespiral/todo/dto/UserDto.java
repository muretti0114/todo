package jp.kobespiral.todo.dto;

import java.util.Date;

import jp.kobespiral.todo.entity.User;
import lombok.Data;

@Data
public class UserDto {
    String uid;
    String name;
    Date createdAt; 
    public static UserDto build(User user) {
        UserDto dto = new UserDto();
        dto.setUid(user.getUid());
        dto.setName(user.getName());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}