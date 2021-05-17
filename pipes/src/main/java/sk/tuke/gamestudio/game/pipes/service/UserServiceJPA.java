package sk.tuke.gamestudio.game.pipes.service;

import sk.tuke.gamestudio.game.pipes.entity.EndUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Transactional
public class UserServiceJPA implements UserService {
    private static final Pattern passwordPattern = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}");
    private static final Pattern loginPattern = Pattern.compile("(?=.*[a-z])(?=\\S+$).{4,}");

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean registerUser(EndUser endUser) throws UserException {
        if(isRegistered(endUser.getPlayer()))
            return false;
        else {
            Matcher passwordMatcher = passwordPattern.matcher(endUser.getPassword());
            Matcher loginMatcher = loginPattern.matcher(endUser.getPlayer());

            if(passwordMatcher.matches() && loginMatcher.matches()) {
                entityManager.persist(endUser);
                return true;
            }
        }
        return false;
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
        if(((Number) entityManager
                .createNamedQuery("EndUser.isPasswordCorrect")
                .setParameter("player", player)
                .setParameter("password", password)
                .getSingleResult()).intValue() > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean isRegistered(String player) throws UserException {
        if(((Number) entityManager.createNamedQuery("EndUser.userExists")
                .setParameter("player", player).getSingleResult()).intValue() > 0){
            return true;
        }
        return false;
    }

    @Override
    public void reset() throws UserException {
        entityManager.createNamedQuery("EndUser.resetUser").executeUpdate();
    }
}
