package sk.tuke.gamestudio.game.pipes.service;

import org.junit.Test;
import sk.tuke.gamestudio.game.pipes.entity.Score;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ScoreServiceTest {

    private final String game = "Pipes";

    private ScoreService createService() {
        //return new ScoreServiceJDBC();
        return new ScoreServiceJPA();
    }

    @Test
    public void testReset() {
        ScoreService service = createService();
        service.reset();
        assertEquals(0, service.getTopScores(game).size());
    }

    @Test
    public void testAddScore() {
        ScoreService service = createService();
        service.reset();
        Date date = new Date();
        service.addScore(new Score(game, "Jaro", 200, date));

        List<Score> scores = service.getTopScores(game);

        assertEquals(1, scores.size());

        assertEquals(game, scores.get(0).getGame());
        assertEquals("Jaro", scores.get(0).getPlayer());
        assertEquals(200, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());
    }

    @Test
    public void testAddScore3() {
        ScoreService service = createService();
        service.reset();
        Date date = new Date();
        service.addScore(new Score(game, "Jaro", 200, date));
        service.addScore(new Score(game, "Fero", 400, date));
        service.addScore(new Score(game, "Jozo", 100, date));

        List<Score> scores = service.getTopScores(game);

        assertEquals(3, scores.size());

        assertEquals(game, scores.get(0).getGame());
        assertEquals("Fero", scores.get(0).getPlayer());
        assertEquals(400, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());

        assertEquals(game, scores.get(1).getGame());
        assertEquals("Jaro", scores.get(1).getPlayer());
        assertEquals(200, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedOn());

        assertEquals(game, scores.get(2).getGame());
        assertEquals("Jozo", scores.get(2).getPlayer());
        assertEquals(100, scores.get(2).getPoints());
        assertEquals(date, scores.get(2).getPlayedOn());
    }

    @Test
    public void testAddScore10() {
        ScoreService service = createService();
        for (int i = 0; i < 20; i++)
            service.addScore(new Score(game, "Jaro", 200, new Date()));
        assertEquals(10, service.getTopScores(game).size());
    }

    @Test
    public void testPersistence() {
        ScoreService service = createService();
        service.reset();
        service.addScore(new Score(game, "Jaro", 200, new Date()));

        service = createService();
        assertEquals(1, service.getTopScores(game).size());
    }

}
