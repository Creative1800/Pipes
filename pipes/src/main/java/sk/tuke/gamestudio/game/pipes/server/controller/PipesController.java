package sk.tuke.gamestudio.game.pipes.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.pipes.core.*;
import sk.tuke.gamestudio.game.pipes.entity.Comment;
import sk.tuke.gamestudio.game.pipes.entity.Rating;
import sk.tuke.gamestudio.game.pipes.entity.Score;
import sk.tuke.gamestudio.game.pipes.service.CommentService;
import sk.tuke.gamestudio.game.pipes.service.RatingService;
import sk.tuke.gamestudio.game.pipes.service.ScoreService;

import java.util.Date;

@Controller
@RequestMapping("/pipes")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PipesController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserController userController;

    private Field field = new Field(1);
    private final HtmlBuilder htmlBuilder = new HtmlBuilder();
    private final String GAME = "pipes";

    private String playerName;
    private boolean isScoreAdded = false;
    private boolean continueNextLevel = false;
    private boolean isMenuShowed = false;
    private boolean isHomeShowed = true;

    @RequestMapping
    public String pipes(@RequestParam(value = "row", required = false) String row,
                        @RequestParam(value = "column", required = false) String column,
                        @RequestParam(value = "group", required = false) TileDirection.Group group,
                        Model model) {

        turnPipe(row, column, group, model);
        fillModel(model);
        return GAME;
    }

    private void turnPipe(String row, String column, TileDirection.Group group, Model model){
        try {
            if(userController.getLoggedUser() != null)
                playerName = userController.getLoggedUser();
            if(field.getGameState() == GameState.PLAYING) {
                isScoreAdded = false;
                field.turnPipe(Integer.parseInt(row), Integer.parseInt(column), group);
            }
            if(field.getGameState() == GameState.SOLVED) {
                if(isGameSolved() && continueNextLevel){
                    nextLevel(model);
                }
            }
            if(field.getGameState() == GameState.SOLVED && !isScoreAdded && playerName != null){
                scoreService.addScore(
                        new Score(
                                GAME,
                                playerName,
                                field.getScore(),
                                new Date()
                        )
                );
                isScoreAdded = true;
            }
        } catch (Exception e) {
            //vraj tu netreba nic pisat
        }
    }

    @RequestMapping("/new")
    public String newGame(@RequestParam(value = "level", required = false) String level, Model model) {
        if(level != null)
            field = new Field(Integer.parseInt(level));
        else
            field = new Field(field.getLevelNumber());
        fillModel(model);
        return GAME;
    }

    @RequestMapping("/nextLevel")
    public String nextLevel(Model model) {
        if(field.getLevelNumber() != field.getLevelsCount())
            field = new Field(field.getLevelNumber()+1);
        fillModel(model);
        return GAME;
    }

    @RequestMapping("/rating")
    public String rating(@RequestParam String rate) {
        ratingService.setRating(new Rating(GAME, userController.getLoggedUser(),Integer.parseInt(rate),new Date()));
        setValues();
        return "redirect:/pipes";
    }

    @RequestMapping("/comment")
    public String comment(String comment) {
        commentService.addComment(new Comment(GAME, userController.getLoggedUser(), comment,new Date()));
        setValues();
        return "redirect:/pipes";
    }

    @RequestMapping("/homep")
    public String homePage() {
        setValues();
        return "redirect:/";
    }

    private String getHtmlField(){
        continueNextLevel = false;
        StringBuilder sb = new StringBuilder();

        if(!isHomeShowed) {
            if (!isMenuShowed) {
                if (!isGameSolved() && !isGameFailed()) {
                    htmlBuilder.getGameBackground(sb, field);

                } else {
                    htmlBuilder.nextLevelForm(sb, field);
                }
            }
        }
        if(isHomeShowed){
            htmlBuilder.gameHomePage(sb);
            isMenuShowed = true;
            isHomeShowed = false;

        } else if(isMenuShowed){
            htmlBuilder.showMenu(sb, field, ratingService);
            isMenuShowed = false;
        }



        return sb.toString();
    }

    @RequestMapping("/home")
    private String home(){
        isHomeShowed = true;

        return "redirect:/pipes";
    }

    @RequestMapping("/menu")
    private String levelMenu(){
        isMenuShowed = true;

        return "redirect:/pipes";
    }


    private void fillModel(Model model) {
        model.addAttribute("scores", scoreService.getTopScores(GAME));
        model.addAttribute("comment", scoreService.getTopScores(GAME));
        model.addAttribute("avgRating", ratingService.getAverageRating(GAME));
        if(ratingService.hasRating(playerName, GAME))
            model.addAttribute("rating", ratingService.getRating(GAME, playerName));
        model.addAttribute("isPlayerLogged", isPlayerLogged());
        model.addAttribute("lastComments", commentService.getComments(GAME));
        model.addAttribute("htmlField", getHtmlField());
    }

    public String getPlayer() {
        return playerName;
    }

    private boolean isPlayerLogged(){
        return playerName != null;
    }

    private boolean isGameSolved(){
        return field.getGameState() == GameState.SOLVED;
    }

    private boolean isGameFailed(){
        return field.getGameState() == GameState.FAILED;
    }

    public void setValues(){
        isMenuShowed = false;
        isHomeShowed = true;
    }

}

