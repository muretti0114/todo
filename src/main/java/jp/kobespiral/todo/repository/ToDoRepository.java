package jp.kobespiral.todo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jp.kobespiral.todo.entity.ToDo;

@Repository
public interface ToDoRepository extends CrudRepository<ToDo, Long> {
    public Iterable<ToDo> findToDoByUidAndIsOpen(String uid, boolean isOpen);
    public Iterable<ToDo> findAllToDoByIsOpen(boolean isOpen);
    public Iterable<ToDo> findAllByUid(String uid);
}