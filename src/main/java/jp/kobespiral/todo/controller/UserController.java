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

import jp.kobespiral.todo.dto.UserForm;
import jp.kobespiral.todo.entity.User;
import jp.kobespiral.todo.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService us;
    
    @GetMapping("")
    String showAllUsers(Model model) {
        List<User> users = us.getAllUsers();
        model.addAttribute("ulist", users);

        return "userlist";
    }

    @GetMapping("/create")
    String showCreateForm(Model model) {
        UserForm form = new UserForm();
        model.addAttribute("postUrl", "users");
        model.addAttribute("isUpdate", false);
        model.addAttribute("userForm", form);
        return "userform";
    }
    @PostMapping("")
    String createUser(@ModelAttribute("userForm") @Validated UserForm form, BindingResult bindingResult,Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("postUrl", "users");
            model.addAttribute("isUpdate", false);
            return "userform";
        }
        us.createUser(form);
        
        return "redirect:/users";
    }

    @GetMapping("/{uid}/update")
    String showUpdateForm(@PathVariable String uid,  Model model) {
        User u = us.getUserByUid(uid);
        model.addAttribute("postUrl", "users/"+uid);
        model.addAttribute("isUpdate", true);
        model.addAttribute("userForm", u);
        return "userform";
    }


    @PostMapping("/{uid}")
    String updateUser(@PathVariable String uid, @ModelAttribute("userForm") @Validated UserForm form, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("postUrl", "users/"+uid);
            model.addAttribute("isUpdate", true);
            return "userform";
        }
        us.updateUser(uid, form);
        
        return "redirect:/users";
    }

    @GetMapping("/{uid}/delete")
    String deleteMapping(@PathVariable String uid) {
        us.deleteUser(uid);

        return "redirect:/users";
    }





}