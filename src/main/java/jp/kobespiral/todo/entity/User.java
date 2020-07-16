package jp.kobespiral.todo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザエンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @Size(max = 16)
    @Pattern(regexp = "[a-z0-9_\\-]+")
    String uid;

    @NotBlank
    @Size(max = 64)
    String name;
    
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt; 
}