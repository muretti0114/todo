package jp.kobespiral.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.kobespiral.todo.dto.ToDoDto;
import jp.kobespiral.todo.dto.ToDoForm;
import jp.kobespiral.todo.entity.ToDo;
import jp.kobespiral.todo.service.ToDoService;

@RestController
@RequestMapping("/api/todos")
public class ToDoRestController {
    @Autowired
    ToDoService ts;

    @PostMapping("")
    public ToDo createToDo(@RequestBody @Validated ToDoForm form) {
        return ts.createToDo(form);
    }
    @PostMapping("/{uid}")
    public ToDo createToDo(@PathVariable String uid, @RequestBody @Validated ToDoForm form) {
        form.setUid(uid);
        return ts.createToDo(form);
    }


    @GetMapping("/todo")
    public List<ToDoDto> getAllToDo() {
        return ts.getAllToDo();
    }
    @GetMapping("/done")
    public List<ToDoDto> getAllDone() {
        return ts.getAllDone();
    }
    @GetMapping("/{uid}/todo")
    public List<ToDoDto> getTodoListByUid(@PathVariable String uid) {
        return ts.getToDoListByUid(uid);
    }

    @GetMapping("/{uid}/done")
    public List<ToDoDto> getDoneListByUid(@PathVariable String uid) {
        return ts.getDoneListByUid(uid);
    }

    @PutMapping("/{uid}/{tid}")
    public ToDo updateToDo(@PathVariable String uid, @PathVariable Long tid, @RequestBody @Validated ToDoForm form) {
        return ts.updateToDo(uid, tid, form);
    }

    @PutMapping("/{uid}/{tid}/done")
    public ToDo done(@PathVariable String uid, @PathVariable Long tid) {
        return ts.done(uid, tid);
    }

    @PutMapping("/{uid}/{tid}/reopen")
    public ToDo reopen(@PathVariable String uid, @PathVariable Long tid) {
        return ts.reopen(uid, tid);
    }




}