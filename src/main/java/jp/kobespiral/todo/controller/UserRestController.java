package jp.kobespiral.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.kobespiral.todo.dto.UserForm;
import jp.kobespiral.todo.entity.User;
import jp.kobespiral.todo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    @Autowired
    UserService us;

    @PostMapping("")
    public User createUser(@RequestBody @Validated UserForm form) {
        return us.createUser(form);
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return us.getAllUsers();
    }

    @GetMapping("/{uid}")
    public User getUser(@PathVariable String uid) {
        return us.getUserByUid(uid);
    }

    @PutMapping("/{uid}")
    public User updateUser(@PathVariable String uid, @RequestBody @Validated UserForm form) {
        return us.updateUser(uid, form);
    }
    
    @DeleteMapping("/{uid}")
    public void deletelUser(@PathVariable String uid) {
        us.deleteUser(uid);
    }

}