package jp.kobespiral.todo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.kobespiral.todo.entity.ToDo;
import jp.kobespiral.todo.entity.User;
import jp.kobespiral.todo.exception.ToDoException;
import jp.kobespiral.todo.repository.ToDoRepository;
import jp.kobespiral.todo.repository.UserRepository;
import jp.kobespiral.todo.dto.UserForm;

/**
 * ユーザ情報を取得するサービス
 */
@Service
public class UserService {
    @Autowired
    UserRepository users;
    @Autowired
    ToDoRepository todos;

    /**
     * フォームを与えてユーザを追加する
     * @param form ユーザフォーム
     * @return 作成したユーザエンティティ
     */
    public User createUser(UserForm form) {
        String uid = form.getUid();
        if (users.findById(uid).isPresent()) {
            throw new ToDoException(ToDoException.USER_ALREADY_EXISTS, uid + ": User already exists.");
        } else {
            User u = form.toEntity();
            return users.save(u);
        }
    }

    /**
     * ユーザをuidで取得する
     * @param uid UID
     * @return ユーザ
     */
    public User getUserByUid(String uid) {
        User u = users.findById(uid).orElseThrow(
            () -> new ToDoException(ToDoException.NO_SUCH_USER, uid + ": No such user")
        );

        return u;
    }

    /**
     * 全ユーザを取得する
     * @return 全ユーザのリスト
     */
    public List<User> getAllUsers() {
        ArrayList <User> list = new ArrayList<>();
        for (User u : users.findAll()) {
            list.add(u);
        }
        return list;
    }

    /**
     * ユーザ名を変更する
     */
    public User updateUser(String uid, UserForm form) {
        User u = getUserByUid(uid);
        u.setName(form.getName());
        return users.save(u);
    }

    /**
     * ユーザを削除する．ユーザに紐づくToDoもすべて消去する
     * @param uid
     */
    @Transactional
    public void deleteUser(String uid) {
        User u = getUserByUid(uid);
        Iterable<ToDo> list = todos.findAllByUid(uid);
        todos.deleteAll(list);
        users.delete(u);
    }


}