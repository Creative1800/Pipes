package sk.tuke.gamestudio.game.pipes.service;

public class RatingException extends RuntimeException {

    public RatingException(String message) {
        super(message);
    }

    public RatingException(String message, Throwable cause) {
        super(message, cause);
    }
}
