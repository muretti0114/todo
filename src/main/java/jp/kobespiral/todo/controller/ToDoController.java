package jp.kobespiral.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.kobespiral.todo.dto.ToDoDto;
import jp.kobespiral.todo.dto.ToDoForm;
import jp.kobespiral.todo.entity.User;
import jp.kobespiral.todo.service.ToDoService;
import jp.kobespiral.todo.service.UserService;

@Controller
@RequestMapping("/todos")
public class ToDoController {
    @Autowired
    ToDoService ts;
    @Autowired
    UserService us;

    /**
     * 管理用コンソールを開く
     * 
     * @param model
     * @return
     */
    @GetMapping("/admin")
    public String showAdminConsole(Model model) {
        List<User> list = us.getAllUsers();
        model.addAttribute("uList", list);
        return "admin";
    }

    /**
     * ユーザ用のToDoリスト一覧を表示
     * 
     * @param uid
     * @param model
     * @return
     */
    @GetMapping("/{uid}")
    public String showToDoListOfUser(@PathVariable String uid, Model model) {
        List<ToDoDto> todos = ts.getToDoListByUid(uid);
        List<ToDoDto> dones = ts.getDoneListByUid(uid);

        // Doneはdone日時が新しいもの順にソート
        dones.sort((a, b) -> a.getDoneAt().before(b.getDoneAt()) ? 1 : -1);

        User user = us.getUserByUid(uid);
        model.addAttribute("todos", todos);
        model.addAttribute("dones", dones);
        model.addAttribute("user", user);
        model.addAttribute("todoForm", new ToDoForm());

        return "todolist";
    }

    /**
     * ユーザ用のToDoリスト一覧を表示
     * 
     * @param uid
     * @param model
     * @return
     */
    @GetMapping("/{uid}/all")
    public String showToDoListOfAll(@PathVariable String uid, Model model) {
        List<ToDoDto> todos = ts.getAllToDo();
        List<ToDoDto> dones = ts.getAllDone();

        // Doneはdone日時が新しいもの順にソート
        dones.sort((a, b) -> a.getDoneAt().before(b.getDoneAt()) ? 1 : -1);

        User user = us.getUserByUid(uid);
        model.addAttribute("todos", todos);
        model.addAttribute("dones", dones);
        model.addAttribute("user", user);
        model.addAttribute("todoForm", new ToDoForm());

        return "todolist";
    }


    /**
     * ユーザのToDoを新規作成する
     * 
     * @param uid   ユーザID
     * @param form  フォーム
     * @param model
     * @return
     */
    @PostMapping("/{uid}")
    public String createToDo(@PathVariable String uid, @ModelAttribute @Validated ToDoForm form, Model model) {
        form.setUid(uid);
        ts.createToDo(form);

        /** 同じページに GETリダイレクト */
        return "redirect:/todos/" + uid;
    }

    @GetMapping("/{uid}/{tid}/done")
    public String doneToDo(@PathVariable String uid, @PathVariable Long tid) {
        ts.done(uid, tid);
        return "redirect:/todos/" + uid;
    }

    @GetMapping("/{uid}/{tid}/reopen")
    public String reopenToDo(@PathVariable String uid, @PathVariable Long tid) {
        ts.reopen(uid, tid);
        return "redirect:/todos/" + uid;
    }

    @GetMapping("/{uid}/{tid}")
    public String getToDo(@PathVariable String uid, @PathVariable Long tid, Model model) {
        User user = us.getUserByUid(uid);
        ToDoDto todo = ts.getToDoDto(tid);
        model.addAttribute("user", user);
        model.addAttribute("todoForm", todo);

        return "todoform";
    }

    @PostMapping("/{uid}/{tid}")
    public String updateToDo(@PathVariable String uid, @PathVariable Long tid, @ModelAttribute @Validated ToDoForm form,
            Model model) {
        ts.updateToDo(uid, tid, form);

        return "redirect:/todos/" + uid;
    }

}