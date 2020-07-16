package jp.kobespiral.todo.dto;

import java.util.Date;

import lombok.Data;

/**
 * ユーザ用のDTO．現状エンティティと同じだが，将来きっと使うはず．
 */
@Data
public class UserDto {
    String uid;
    String name;
    Date createdAt; 
}