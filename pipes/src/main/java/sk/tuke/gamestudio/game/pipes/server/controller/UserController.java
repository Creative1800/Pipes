package sk.tuke.gamestudio.game.pipes.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.pipes.entity.EndUser;
import sk.tuke.gamestudio.game.pipes.service.UserService;



@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    PipesController pipesController = new PipesController();

    @Autowired
    UserService userService;

    private boolean isLogged = false;
    private String loggedUser;

    @RequestMapping("/login")
    public String login(String login, String password){
        String loginToLower = login.toLowerCase();
        //pipesController.setValues();
        if(userService.userAuthentication(
                new EndUser(
                        loginToLower ,
                        password
                )
        )) {
            loggedUser = loginToLower;
            isLogged = true;
            return "redirect:/";
        }
        return "redirect:/";
    }

    @RequestMapping("/register")
    public String register(String login, String password){
        String loginToLower = login.toLowerCase();
        userService.registerUser(
                new EndUser(
                            loginToLower,
                            password
                ));
        isLogged = false;
        pipesController.setValues();
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(){
        loggedUser = null;
        isLogged = false;

        return "redirect:/";
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public boolean isLoggedUser(){
        return loggedUser != null;
    }
}
