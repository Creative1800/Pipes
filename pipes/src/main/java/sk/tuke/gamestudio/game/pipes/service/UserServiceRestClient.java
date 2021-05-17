package sk.tuke.gamestudio.game.pipes.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.pipes.entity.EndUser;

public class UserServiceRestClient implements UserService {

    private final String url = "http://localhost:8080/api/user";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean registerUser(EndUser endUser) throws UserException {
        if(isRegistered(endUser.getPlayer())) return false;
        else {
            restTemplate.postForEntity(url + "/user", endUser, EndUser.class);
            return true;
        }
    }

    @Override
    public boolean userAuthentication(EndUser endUser) throws UserException {
        if(isRegistered(endUser.getPlayer()) == true &&
                correctPassword(endUser.getPlayer(), endUser.getPassword())){
            return true;
        }
        return false;
    }

    private boolean correctPassword(String player, String password) {
        return false;
    }

    @Override
    public boolean isRegistered(String player) throws UserException {
        return false;
    }

    @Override
    public void reset() throws UserException {

    }
}
