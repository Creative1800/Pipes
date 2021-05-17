/*
package sk.tuke.gamestudio.game.pipes.consoleui;


import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.pipes.entity.Comment;
import sk.tuke.gamestudio.game.pipes.entity.Rating;
import sk.tuke.gamestudio.game.pipes.service.CommentService;
import sk.tuke.gamestudio.game.pipes.service.RatingService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUIInput {
    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private static final Pattern namePattern = Pattern.compile("([A-Za-z]{3,10})");
    private static final Pattern ratingPattern = Pattern.compile("([1-5])");
    private static final Pattern ynPattern = Pattern.compile("([Yy,Nn])");
    private static final Pattern commentPattern = Pattern.compile("([A-Za-z]{1,200})");


    private final String GAME = "Pipes";
    private final Scanner scanner = new Scanner(System.in);
    private String playerName;
    private int counter = 0;

    public void selectPlayerName() {
        System.out.print("Enter name (One word, 3-10 letters): ");
        playerName = scanner.nextLine();

        Matcher matcher = namePattern.matcher(playerName);
        if (matcher.matches()) {
            playerName = playerName.substring(0, 1).toUpperCase() + playerName.substring(1);
        } else {
            System.out.println("Wrong name!");
            selectPlayerName();
        }
    }

    public void addRatingOfGame() {
        System.out.println("Would you like to rate the game? (Y/N): ");
        String yn = scanner.nextLine().toUpperCase();

        Matcher matcher = ynPattern.matcher(yn);
        if (matcher.matches()) {
            if ("Y".equals(yn)) {
                System.out.println("Enter your rating (worst 1-5 best): ");
                String line = scanner.nextLine();

                Matcher ratingMatcher = ratingPattern.matcher(line);
                while (!ratingMatcher.matches()) {
                    System.out.println("Enter number from 1 to 5!");
                    line = scanner.nextLine();
                    ratingMatcher = ratingPattern.matcher(line);
                }
                if (ratingMatcher.matches()) {
                    int gameRating = Integer.parseInt(ratingMatcher.group(1));
                    ratingService.setRating(
                            new Rating(
                                    GAME,
                                    playerName,
                                    gameRating,
                                    new Date()
                            ));
                }
            }
            counter++;
        }
    }

    public void addCommentToGame() {
        System.out.println("Would you like to comment the game? (Y/N): ");
        String yn = scanner.nextLine().toUpperCase();

        Matcher matcher = ynPattern.matcher(yn);
        if (matcher.matches()) {
            if ("Y".equals(yn)) {
                System.out.println("Enter your comment (1-200 words): ");
                String comment = scanner.nextLine();

                Matcher commentMatcher = commentPattern.matcher(comment);
                if (commentMatcher.matches()) {
                    commentService.addComment(
                            new Comment(
                                    GAME,
                                    playerName,
                                    comment,
                                    new Date()
                            )
                    );
                }
            }
            counter++;
        }
    }

    public void showLastComment() {
        System.out.println("Last Comments: ");
        System.out.println();
        List<Comment> comments = commentService.getComments(GAME);
        for (Comment comment : comments) {
            System.out.printf("%s \t %s \n", comment.getPlayer(), comment.getComment());
        }
        System.out.println("------------------------------------");
        System.out.println();
        counter++;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getCounter() {
        return counter;
    }
}
*/
