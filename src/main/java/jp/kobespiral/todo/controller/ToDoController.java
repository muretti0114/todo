package jp.kobespiral.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

/**
 * ToDoのコントローラ
 */
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
     * @return 管理者用ページを表示
     */
    @GetMapping("/Administrator")
    public String showAdminConsole(Model model) {
        List<User> list = us.getAllUsers();
        model.addAttribute("uList", list);
        return "admin";
    }

    /**
     * ユーザ用のToDoリスト一覧を表示
     * 
     * @param uid   ユーザID
     * @param model
     * @return ToDoリストのページを表示
     */
    @GetMapping("/{uid}")
    public String showToDoListOfUser(@PathVariable String uid, Model model) {
        // そのユーザのToDoリスト
        List<ToDoDto> todos = ts.getToDoListByUid(uid);
        // そのユーザのDoneリスト
        List<ToDoDto> dones = ts.getDoneListByUid(uid);

        // Doneはdone日時が新しいもの順にソート
        dones.sort((a, b) -> a.getDoneAt().before(b.getDoneAt()) ? 1 : -1);

        User user = us.getUserByUid(uid);
        ToDoForm blankForm = new ToDoForm();

        // テンプレートにオブジェクトをセットする
        model.addAttribute("todos", todos);
        model.addAttribute("dones", dones);
        model.addAttribute("user", user);
        model.addAttribute("toDoForm", blankForm);

        return "todolist";
    }

    /**
     * 全ユーザのToDoリスト一覧を表示
     * 
     * @param uid   ユーザID
     * @param model
     * @return 全ユーザのToDoリストのページを表示
     */
    @GetMapping("/{uid}/all")
    public String showToDoListOfAll(@PathVariable String uid, Model model) {
        // すべてのユーザのToDoリスト
        List<ToDoDto> todos = ts.getAllToDo();
        // すべてのユーザのDoneリスト
        List<ToDoDto> dones = ts.getAllDone();

        // Doneはdone日時が新しいもの順にソート
        dones.sort((a, b) -> a.getDoneAt().before(b.getDoneAt()) ? 1 : -1);

        User user = us.getUserByUid(uid);
        ToDoForm blankForm = new ToDoForm();

        // テンプレートにオブジェクトをセットする
        model.addAttribute("todos", todos);
        model.addAttribute("dones", dones);
        model.addAttribute("user", user);
        model.addAttribute("toDoForm", blankForm);

        return "todolist";
    }

    /**
     * あるユーザのToDoを新規作成する
     * 
     * @param uid           ユーザID
     * @param form          テンプレートから渡されるフォーム
     * @param bindingResult バリデーションの結果
     * @param model
     * @return ToDoリストのページを表示
     */
    @PostMapping("/{uid}")
    public String createToDo(@PathVariable String uid, @ModelAttribute("toDoForm") @Validated ToDoForm form,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // エラーがあったら"toDoForm" にエラー結果が自動セットされる．
            // ↓ ToDo一覧ページの表示に必要なその他の情報をセット
            model.addAttribute("todos", ts.getToDoListByUid(uid));
            model.addAttribute("dones", ts.getDoneListByUid(uid));
            model.addAttribute("user", us.getUserByUid(uid));
            return "todolist";
        }

        /* バリデーションOKの場合はToDoエンティティを作成する */
        ts.createToDo(uid, form);

        /* 同じページに GETリダイレクト */
        return "redirect:/todos/" + uid;
    }

    /**
     * ToDoを閉じる
     * @param uid 現在のユーザ
     * @param tid ToDoのID
     * @return ToDoリストのページを表示
     */
    @GetMapping("/{uid}/{tid}/done")
    public String doneToDo(@PathVariable String uid, @PathVariable Long tid) {
        ts.done(uid, tid);
        return "redirect:/todos/" + uid;
    }

    /**
     * ToDoを再開する
     * @param uid 現在のユーザ
     * @param tid ToDoのID
     * @return ToDoリストのページを表示
     */
    @GetMapping("/{uid}/{tid}/reopen")
    public String reopenToDo(@PathVariable String uid, @PathVariable Long tid) {
        ts.reopen(uid, tid);
        return "redirect:/todos/" + uid;
    }

    /**
     * ToDoの詳細を表示する
     * @param uid 現在のユーザ
     * @param tid ToDoのID
     * @param model
     * @return ToDoの確認・修正フォームを表示
     */
    @GetMapping("/{uid}/{tid}")
    public String getToDo(@PathVariable String uid, @PathVariable Long tid, Model model) {
        ToDoDto todo = ts.getToDoDto(tid);
        model.addAttribute("userId", uid);
        model.addAttribute("todo", todo);

        return "todoform";
    }

    /**
     * ToDoの内容を更新する
     * @param uid 現在のユーザ
     * @param tid ToDoのID
     * @param form テンプレートから渡されるフォーム
     * @param bindingResult バリデーションの結果
     * @param model
     * @return ToDoリストのページを表示
     */
    @PostMapping("/{uid}/{tid}")
    public String updateToDo(@PathVariable String uid, @PathVariable Long tid,
            @ModelAttribute("todo") @Validated ToDoDto form, 
            BindingResult bindingResult, Model model) {
        
        if (bindingResult.hasErrors()) {
            // エラーがあったら"todo" にエラー結果が自動セットされる．
            // ↓ ToDo確認・修正ページの表示に必要なその他の情報をセット
            model.addAttribute("userId", uid);
            System.err.println("ERROR-----------");
            System.err.println(bindingResult);
            return "todoform";
        }

        //バリデーションOKなら更新
        ts.updateToDo(uid, tid, form);
        //ToDo一覧ページを表示
        return "redirect:/todos/" + uid;
    }

}