package jp.kobespiral.todo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.kobespiral.todo.dto.ToDoDto;
import jp.kobespiral.todo.dto.ToDoForm;
import jp.kobespiral.todo.entity.ToDo;
import jp.kobespiral.todo.entity.User;
import jp.kobespiral.todo.exception.ToDoException;
import jp.kobespiral.todo.repository.ToDoRepository;
import jp.kobespiral.todo.repository.UserRepository;

@Service
public class ToDoService {
    @Autowired
    UserRepository users;
    @Autowired
    ToDoRepository todos;

    /**
     * ToDoを作成する
     * 
     * @param form
     * @return
     */
    public ToDo createToDo(ToDoForm form) {
        ToDo t = form.toEntity();
        String uid = t.getUid();
        if (!users.existsById(uid)) {
            throw new ToDoException(ToDoException.NO_SUCH_USER, uid + ": No such user.");
        }
        return todos.save(t);
    }

    public ToDo getToDo(Long tid) {
        ToDo t = todos.findById(tid)
                .orElseThrow(() -> new ToDoException(ToDoException.NO_SUCH_TODO, tid + ": No such ToDo"));
        return t;
    }

    /**
     * ToDoを更新する
     * 
     * @param uid
     * @return
     */
    public ToDo updateToDo(String uid, Long tid, ToDoForm form) {
        ToDo t = getToDo(tid);
        if (!uid.equals(t.getUid())) {
            throw new ToDoException(ToDoException.OPERATION_NOT_AUTHORIZED, "Cannot update other's ToDo");
        }
        t.setTitle(form.getTitle());
        t.setDescription(form.getDescription());
        return todos.save(t);
    }

    /**
     * ToDoをIDで1つ取得する
     * 
     * @param tid
     * @return
     */
    public ToDoDto getToDoDto(Long tid) {
        ToDo t = getToDo(tid);
        return createDto(t);
    }

    /**
     * ユーザのToDoのリストを取得する
     * 
     * @param uid
     * @return
     */
    public List<ToDoDto> getToDoListByUid(String uid) {
        ArrayList<ToDoDto> list = new ArrayList<>();
        User u = getUser(uid);
        for (ToDo t : todos.findToDoByUidAndIsOpen(uid, true)) {
            list.add(createDto(t, u));
        }
        return list;
    }

    /**
     * ユーザのDoneのリストを取得する
     * 
     * @param uid
     * @return
     */
    public List<ToDoDto> getDoneListByUid(String uid) {
        ArrayList<ToDoDto> list = new ArrayList<>();
        User u = getUser(uid);
        for (ToDo t : todos.findToDoByUidAndIsOpen(uid, false)) {
            list.add(createDto(t, u));
        }

        return list;
    }

    /**
     * 全ユーザDoneリストを取得する
     * 
     * @return
     */
    public List<ToDoDto> getAllDone() {
        ArrayList<ToDoDto> list = new ArrayList<>();
        for (ToDo t : todos.findAll()) {
            if (!t.isOpen()) list.add(createDto(t));
        }
        return list;
    }
    /**
     * 全ユーザのToDoリストを取得する
     * 
     * @return
     */
    public List<ToDoDto> getAllToDo() {
        ArrayList<ToDoDto> list = new ArrayList<>();
        for (ToDo t : todos.findAll()) {
            if (t.isOpen()) list.add(createDto(t));
        }
        return list;
    }

    /**
     * 指定したToDoをDoneにする
     * 
     * @param tid
     */
    public ToDo done(String uid, Long tid) {
        ToDo t = getToDo(tid);

        if (!uid.equals(t.getUid())) {
            throw new ToDoException(ToDoException.OPERATION_NOT_AUTHORIZED, "Cannot modify other's ToDo");
        }

        if (t.isOpen()) {
            t.setOpen(false);
            t.setDoneAt(new Date());
            return todos.save(t);
        } else {
            throw new ToDoException(ToDoException.TODO_ALREADY_DONE, tid + ": Already done");
        }
    }

    /**
     * 指定したToDoをReopenする
     * 
     * @param tid
     */
    public ToDo reopen(String uid, Long tid) {
        ToDo t = getToDo(tid);
        if (!uid.equals(t.getUid())) {
            throw new ToDoException(ToDoException.OPERATION_NOT_AUTHORIZED, "Cannot modify other's ToDo");
        }

        if (!t.isOpen()) {
            t.setOpen(true);
            t.setDoneAt(null);
            t.setUpdatedAt(new Date());
            return todos.save(t);
        } else {
            throw new ToDoException(ToDoException.TODO_NOT_DONE, tid + ": Cannot reopen undone ToDo");
        }
    }

    /**
     * ToDoからToDoDTOを作成するユーティリティ関数
     * @param todo
     * @return
     */
    private ToDoDto createDto(ToDo todo) {
        User user = getUser(todo.getUid());
        ToDoDto dto = new ToDoDto();
        dto.setTid(todo.getTid());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setOpen(todo.isOpen());
        dto.setCreatedAt(todo.getCreatedAt());
        dto.setUpdateAt(todo.getUpdatedAt());
        dto.setDoneAt(todo.getDoneAt());
        dto.setUid(user.getUid());
        dto.setName(user.getName());
        return dto;
    }

    /**
     * ToDoからToDoDTOを作成するユーティリティ関数
     * @param todo
     * @return
     */
    private ToDoDto createDto(ToDo todo, User user) {
        ToDoDto dto = new ToDoDto();
        dto.setTid(todo.getTid());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setOpen(todo.isOpen());
        dto.setCreatedAt(todo.getCreatedAt());
        dto.setUpdateAt(todo.getUpdatedAt());
        dto.setDoneAt(todo.getDoneAt());
        dto.setUid(user.getUid());
        dto.setName(user.getName());
        return dto;
    }

    private User getUser(String uid) {
        User user = users.findById(uid)
                .orElseThrow(() -> new ToDoException(ToDoException.NO_SUCH_USER, uid + ": No such User"));
        return user;

    }

}