package sk.tuke.gamestudio.game.pipes.service;

import sk.tuke.gamestudio.game.pipes.entity.EndUser;

public interface UserService {

    boolean registerUser(EndUser endUser) throws UserException;

    boolean userAuthentication(EndUser endUser) throws UserException;

    boolean isRegistered(String player) throws UserException;

    void reset() throws UserException;

}
