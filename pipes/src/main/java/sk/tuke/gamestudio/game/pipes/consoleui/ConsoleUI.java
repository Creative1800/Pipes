package sk.tuke.gamestudio.game.pipes.consoleui;

import org.jetbrains.annotations.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.pipes.core.*;
import sk.tuke.gamestudio.game.pipes.entity.Comment;
import sk.tuke.gamestudio.game.pipes.entity.Rating;
import sk.tuke.gamestudio.game.pipes.entity.Score;
import sk.tuke.gamestudio.game.pipes.service.CommentService;
import sk.tuke.gamestudio.game.pipes.service.RatingService;
import sk.tuke.gamestudio.game.pipes.service.ScoreService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConsoleUI {
    private Field field;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private static final Pattern commandPattern = Pattern.compile("([A-I])([1-9])");
    private static final Pattern namePattern = Pattern.compile("([A-Za-z]{3,10})");
    private static final Pattern ratingPattern = Pattern.compile("([1-5])");
    private static final Pattern ynPattern = Pattern.compile("([Yy,Nn])");
    private static final Pattern commentPattern = Pattern.compile("([A-Za-z]{1,200})");


    private final Scanner scanner = new Scanner(System.in);

    private final String GAME = "pipes";
    private String playerName;

    private int levelNumber;
    private int score = 0;
    private int counter = 0;


    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play() {

        if (playerName == null) {
            //printTopScores();

            selectPlayerName();
        }

        do {
            printGame();
            processInput();
        } while (field.getGameState() == GameState.PLAYING);

        levelNumber = field.getLevelNumber();
        handleScore();

        printGame();
        printGameState();

        if (inputToNextLevel() && field.getLevelsCount() != levelNumber) {
            levelNumber++;
            field = new Field(levelNumber);
            play();
        }

        addScore();
        if (getCounter() != 3) {
            addRatingOfGame();
            printAvgRating();
            showLastComment();
            addCommentToGame();
        }
    }

    private void printGame() {
        printMovesCounterAndName();

        printColumnNumbers();

        printField();
    }

    private void printMovesCounterAndName() {
        System.out.println("Player: " + playerName + "          Moves: " + field.getMoves() + "     Level: " + field.getLevelNumber());
        System.out.println();
    }

    private void printColumnNumbers() {
        System.out.print(" ");
        for (int column = 0; column < field.getColumnCount(); column++) {
            if (column == 0) {
                System.out.print("   ");
                continue;
            }
            if (column == field.getColumnCount() - 1) {
                System.out.print(" | ");
                continue;
            }
            System.out.print(" | ");
            System.out.print(column);
        }
    }

    private void printField() {
        System.out.println();

        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print((char) (row + 'A'));
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                if (column == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(" | ");
                }
                switch (tile.getTileGroup()) {
                    case emptyTile -> printEmptyTile(tile);
                    case straightPipe -> printStraightPipe(tile);
                    case rightAngledPipe -> printRightAngledPipe(tile);
                    case triplePipe -> printTriplePipe(tile);
                    case startEndTile -> printStartEndTile(tile);
                    default -> throw new IllegalArgumentException("Unsupported tile state");
                }
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------");
        System.out.println();
    }

    private void processInput() {
        System.out.print("Enter command (X - exit, A0 - turn pipe on position A0): ");
        String line = scanner.nextLine().toUpperCase();
        if ("X".equals(line))
            System.exit(0);

        Matcher matcher = commandPattern.matcher(line);
        if (matcher.matches()) {
            int row = line.charAt(0) - 'A';
            int column = Integer.parseInt(matcher.group(2));

            if (field.getTile(row, column).getTileGroup() != TileDirection.Group.emptyTile)
                field.turnPipe(row, column, field.getTile(row, column).getTileGroup());
            else {
                System.out.println();
                System.out.println("            Cannot turn empty tile!");
                System.out.println();
            }
        }
    }

    public boolean inputToNextLevel() {
        if (levelNumber == field.getLevelsCount()) {
            System.out.println("You won all levels! Great job!");
            return false;
        }
        System.out.print("Continue to next level (y/n): ");
        String line = scanner.nextLine().toUpperCase();
        if ("Y".equals(line)) {
            System.out.println("--------------------------------");
            return true;
        }
        return false;
    }

    private void handleScore() {
        score += field.getScore();
    }

    private void addScore() {
        scoreService.addScore(
                new Score(
                        GAME,
                        playerName,
                        score,
                        new Date()
                )
        );
    }

    public void printTopScores() {
        System.out.println("Top scores: ");
        System.out.println();
        List<Score> scores = scoreService.getTopScores(GAME);
        for (Score score : scores) {
            System.out.printf("%s \t %d \n", score.getPlayer(), score.getPoints());
        }
        System.out.println("------------------------------------");
        System.out.println();
    }

    public void printEmptyTile(@NotNull Tile tile) {
        if (tile.getTileDirection() == TileDirection.EMPTY && tile instanceof TileEmpty) {
            System.out.print(" ");
        }
    }

    public void printRightAngledPipe(@NotNull Tile tile) {
        if (tile instanceof TilePipe) {
            switch (tile.getTileDirection()) {
                case NORTHEAST -> System.out.print("╚");
                case SOUTHEAST -> System.out.print("╔");
                case SOUTHWEST -> System.out.print("╗");
                case NORTHWEST -> System.out.print("╝");
            }
        }
    }

    public void printStraightPipe(Tile tile) {
        if (tile instanceof TilePipe) {
            switch (tile.getTileDirection()) {
                case NORTHSOUTH -> System.out.print("║");
                case EASTWEST -> System.out.print("═");
            }
        }
    }

    public void printTriplePipe(Tile tile) {
        if (tile instanceof TilePipe) {
            switch (tile.getTileDirection()) {
                case WESTNORTHEAST -> System.out.print("╩");
                case NORTHEASTSOUTH -> System.out.print("╠");
                case EASTSOUTHWEST -> System.out.print("╦");
                case SOUTHWESTNORTH -> System.out.print("╣");
            }
        }
    }

    private void printStartEndTile(Tile tile) {
        if (tile instanceof TileStartEnd) {
            switch (tile.getTileDirection()) {
                case START -> System.out.print("╚");
                case END -> System.out.print("═");
            }
        }
    }

    private void printGameState() {
        switch (field.getGameState()) {
            case FAILED -> {
                System.out.println("Game Over: No moves!");
                if (levelNumber > 1) {
                    System.out.println(playerName + ", your score is: " + score);
                }
            }
            case SOLVED -> {
                System.out.println();
                System.out.println("You Won \n" + playerName + ", your score is: " + score);
                System.out.println();
            }

        }
    }

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

    private void printAvgRating() {
        System.out.print("Avarage rating: ");
        System.out.print(ratingService.getAverageRating(GAME));
        System.out.println();
        System.out.println();
    }

    private void printRating() {
        System.out.print("Rating: ");
        System.out.print(ratingService.getRating(GAME, "Samuel"));
    }


    public int getCounter() {
        return counter;
    }
}