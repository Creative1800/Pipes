package sk.tuke.gamestudio.game.pipes.service;

import org.junit.Test;
import sk.tuke.gamestudio.game.pipes.entity.Rating;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RatingServiceTest {

    private final String game = "Pipes";
    private final String playerName = "Jozo";

    @Test
    public void testGetRating() {
        RatingService service = new RatingServiceJDBC();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating(game, playerName, 2, date));
        int ratings = service.getRating(game, playerName);
        assertEquals(2, ratings);
    }

    @Test
    public void testGetAverageRating() {
        RatingService service = new RatingServiceJDBC();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating(game, "jano", 1, date));
        service.setRating(new Rating(game, "mino", 3, date));
        service.setRating(new Rating(game, playerName, 5, date));
        service.setRating(new Rating(game, "matus", 4, date));
        service.setRating(new Rating(game, "marek", 5, date));
        service.setRating(new Rating(game, "fero", 3, date));
        int avgRating = service.getAverageRating(game);
        assertEquals(4, avgRating);
    }

    @Test
    public void testGetRatingDifferentGame() {
        RatingService service = new RatingServiceJDBC();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating(game, playerName, 1, date));
        service.setRating(new Rating("mines", playerName, 3, date));

        int rating = service.getRating(game, playerName);
        assertEquals(1, rating);
    }

    @Test
    public void samePlayerRatingTest() {
        RatingService service = new RatingServiceJDBC();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating(game, playerName, 1, date));
        service.setRating(new Rating(game, playerName, 3, date));

        int rating = service.getRating(game, playerName);
        assertEquals(3, rating);
    }

}
