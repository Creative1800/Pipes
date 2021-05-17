package sk.tuke.gamestudio.game.pipes.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@NamedQuery( name = "EndUser.userExists",
        query = "SELECT count(player) from EndUser WHERE player =: player")
@NamedQuery( name = "EndUser.isPasswordCorrect",
        query = "SELECT count(player) from EndUser WHERE player =: player and password =: password")
@NamedQuery( name = "EndUser.getPlayer",
        query = "SELECT player from EndUser where player =: player")
@NamedQuery( name = "EndUser.resetUser",
        query = "DELETE FROM EndUser ")
public class EndUser implements Serializable {

    @Id
    String player;

    String password;


    public EndUser(String player, String password) {
        this.player = player;
        this.password = password;
    }

    public EndUser() {

    }

    public String getPlayer() {
        return player;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                ", player='" + player + '\'' +
                ", password='"+ password + '\'' +
                '}';
    }
}


