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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ToDo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long tid;

    @NotBlank
    @Size(max=32)
    String title;

    @Size(max=512)
    String description;

    boolean isOpen;

    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    Date doneAt;

    @NotBlank
    String uid;
}