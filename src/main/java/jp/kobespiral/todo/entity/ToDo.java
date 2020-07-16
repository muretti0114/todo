package jp.kobespiral.todo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ToDoエンティティ
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ToDo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long tid;           //ID．自動採番

    @NotBlank
    @Size(max=32)
    String title;       //タイトル

    @Size(max=512)
    String description; //詳細説明

    boolean isOpen;    //未処理の状態か

    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;    //作成日時

    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;    //更新日時

    @Temporal(TemporalType.TIMESTAMP)
    Date doneAt;       //完了日時

    @NotBlank
    String uid; //ユーザへの外部キー
}