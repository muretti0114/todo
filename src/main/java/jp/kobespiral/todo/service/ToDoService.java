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

/**
 * ToDoアプリのドメインサービス
 */
@Service
public class ToDoService {
    @Autowired
    UserRepository users;
    @Autowired
    ToDoRepository todos;

    /* -C-C-C-C-C-C-C-C-C-C-C-C-C- 生成系サービス -C-C-C-C-C-C-C-C-C-C-C-C-C- */

    /**
     * ToDoを作成する
     * 
     * @param uid  作成者
     * @param form ToDoフォーム
     * @return 作成されたToDoエンティティ
     */
    public ToDo createToDo(String uid, ToDoForm form) {

        // uidの重複チェック
        if (!users.existsById(uid)) {
            throw new ToDoException(ToDoException.NO_SUCH_USER, uid + ": No such user.");
        }

        // ここでエンティティを作って
        ToDo t = new ToDo(null, form.getTitle(), form.getDescription(), true, new Date(), new Date(), null, uid);
        // セーブ
        return todos.save(t);
    }

    /**
     * ToDoからToDoDTOを作成する（ユーティリティ関数）
     * 
     * @param todo
     * @return ToDoのDTO
     */
    private ToDoDto createDto(ToDo todo) {
        User user = getUser(todo.getUid());
        return createDto(todo, user);
    }

    /**
     * ユーザを与えて，ToDoからToDoDTOを作成する（ユーティリティ関数）
     * 
     * @param todo
     * @param user
     * @return ToDoのDTO
     */
    private ToDoDto createDto(ToDo todo, User user) {
        ToDoDto dto = new ToDoDto();
        dto.setTid(todo.getTid());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setOpen(todo.isOpen());
        dto.setCreatedAt(todo.getCreatedAt());
        dto.setUpdatedAt(todo.getUpdatedAt());
        dto.setDoneAt(todo.getDoneAt());
        dto.setUid(user.getUid());
        dto.setName(user.getName());
        return dto;
    }

    /* -R-R-R-R-R-R-R-R-R-R-R-R-R- 取得系サービス -R-R-R-R-R-R-R-R-R-R-R-R-R- */

    /**
     * ToDoエンティティを取得する
     * 
     * @param tid
     * @return
     */
    public ToDo getToDo(Long tid) {
        ToDo t = todos.findById(tid)
                .orElseThrow(() -> new ToDoException(ToDoException.NO_SUCH_TODO, tid + ": No such ToDo"));
        return t;
    }

    /**
     * ユーザエンティティを取得する
     * 
     * @param uid ユーザID
     * @return
     */
    private User getUser(String uid) {
        User user = users.findById(uid)
                .orElseThrow(() -> new ToDoException(ToDoException.NO_SUCH_USER, uid + ": No such User"));
        return user;

    }

    /**
     * ToDoのDTOを取得する
     * 
     * @param tid
     * @return
     */
    public ToDoDto getToDoDto(Long tid) {
        ToDo t = getToDo(tid);
        return createDto(t);
    }

    /**
     * ユーザのToDo(DTO)のリストを取得する
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
     * ユーザのDone(DTO)のリストを取得する
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
     * 全ユーザのToDoリストを取得する
     * 
     * @return
     */
    public List<ToDoDto> getAllToDo() {
        ArrayList<ToDoDto> list = new ArrayList<>();
        for (ToDo t : todos.findAll()) {
            if (t.isOpen())
                list.add(createDto(t));
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
            if (!t.isOpen())
                list.add(createDto(t));
        }
        return list;
    }

    /* -U-U-U-U-U-U-U-U-U-U-U-U-U- 更新系サービス -U-U-U-U-U-U-U-U-U-U-U-U-U- */

    /**
     * ToDoを更新する
     * 
     * @param uid  更新するユーザ
     * @param tid  更新対象のToDo
     * @param form 更新フォーム
     * @return 更新されたToDoエンティティ
     */
    public ToDo updateToDo(String uid, Long tid, ToDoDto form) {
        ToDo t = getToDo(tid);

        // 更新権限のチェック
        if (!uid.equals(t.getUid())) {
            throw new ToDoException(ToDoException.OPERATION_NOT_AUTHORIZED, "Cannot update other's ToDo");
        }
        // フォームからタイトルと詳細説明のみを取り出して，更新
        t.setTitle(form.getTitle());
        t.setDescription(form.getDescription());
        // 更新日時をセット
        t.setUpdatedAt(new Date());
        // セーブ
        return todos.save(t);
    }

    /**
     * 指定したToDoをDoneにする
     * 
     * @param uid 更新するユーザ
     * @param tid 更新対象のToDo
     * @return
     */
    public ToDo done(String uid, Long tid) {
        ToDo t = getToDo(tid);

        // 更新権限のチェック
        if (!uid.equals(t.getUid())) {
            throw new ToDoException(ToDoException.OPERATION_NOT_AUTHORIZED, "Cannot modify other's ToDo");
        }
        // すでにDoneでないかチェック
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
     * @param uid 更新するユーザ
     * @param tid 更新対象のToDo
     */
    public ToDo reopen(String uid, Long tid) {
        ToDo t = getToDo(tid);

        // 権限チェック
        if (!uid.equals(t.getUid())) {
            throw new ToDoException(ToDoException.OPERATION_NOT_AUTHORIZED, "Cannot modify other's ToDo");
        }

        // Done済かどうかをチェック
        if (!t.isOpen()) {
            t.setOpen(true);
            t.setDoneAt(null);
            t.setUpdatedAt(new Date());
            return todos.save(t);
        } else {
            throw new ToDoException(ToDoException.TODO_NOT_DONE, tid + ": Cannot reopen undone ToDo");
        }
    }

    /* -D-D-D-D-D-D-D-D-D-D-D-D-D- 削除系サービス -D-D-D-D-D-D-D-D-D-D-D-D-D- */

    /**
     * 指定したToDoを削除する
     * 
     * @param uid 更新するユーザ
     * @param tid 更新対象のToDo
     * @return
     */
    public void deleteToDo(String uid, Long tid) {
        ToDo t = getToDo(tid);

        // 権限チェック
        if (!uid.equals(t.getUid())) {
            throw new ToDoException(ToDoException.OPERATION_NOT_AUTHORIZED, "Cannot delete other's ToDo");
        }

        todos.deleteById(tid);

    }
}