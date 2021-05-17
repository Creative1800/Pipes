package sk.tuke.gamestudio.game.pipes.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQuery( name = "Comment.getComments",
        query = "SELECT s FROM Comment s WHERE s.game=:game ORDER BY s.commentedOn DESC")
@NamedQuery( name = "Comment.resetComments",
        query = "DELETE FROM Comment ")
public class Comment implements Serializable {
    @Id
    @GeneratedValue
    private int ident;

    private String player;
    private String comment;
    private Date commentedOn;
    private String game;

    public Comment(String game, String player, String comment, Date commentedOn) {
        this.game = game;
        this.player = player;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }

    public Comment(){}

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public String getComment() {
        return comment;
    }


    public Date getCommentedOn() {
        return commentedOn;
    }

    public int getIdent(){
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", comment=" + comment +
                ", playedOn=" + commentedOn +
                '}';
    }
}
