package sk.tuke.gamestudio.game.pipes.service;

import org.junit.Test;
import sk.tuke.gamestudio.game.pipes.entity.Comment;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class CommentServiceTest {

    private final String game = "Pipes";

    private CommentService createService() {
        return new CommentServiceJDBC();
    }

    @Test
    public void testReset() {
        CommentService service = createService();
        service.reset();
        assertEquals(0, service.getComments(game).size());
    }

    @Test
    public void testAddComment() {
        CommentService service = createService();
        service.reset();
        Date date = new Date();
        service.addComment(new Comment(game, "Jaro", "This game is great!", date));

        List<Comment> comments = service.getComments(game);

        assertEquals(1, comments.size());

        assertEquals(game, comments.get(0).getGame());
        assertEquals("Jaro", comments.get(0).getPlayer());
        assertEquals("This game is great!", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
    }

    @Test
    public void testComment() {
        CommentService service = createService();
        service.reset();
        Date date = new Date();
        service.addComment(new Comment(game, "Jaro", "yes, i won", date));
        service.addComment(new Comment(game, "Jozo", "amazing game", date));
        service.addComment(new Comment(game, "Jano", "Great game", date));
        service.addComment(new Comment(game, "Feri", "The game is awesome", date));

        List<Comment> comment = service.getComments(game);

        assertEquals(4, comment.size());

        assertEquals("yes, i won", comment.get(0).getComment());
        assertEquals("Jozo", comment.get(1).getPlayer());
        assertEquals(game, comment.get(2).getGame());
        assertEquals("The game is awesome", comment.get(3).getComment());
    }

}

