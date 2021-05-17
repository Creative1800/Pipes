package sk.tuke.gamestudio.game.pipes.service;

import sk.tuke.gamestudio.game.pipes.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        entityManager.merge(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return ((Number) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getSingleResult()).intValue();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return ((Number) entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game).setParameter("player", player).getSingleResult()).intValue();
    }

    @Override
    public boolean hasRating(String player, String game) throws UserException {
        if(((Number) entityManager.createNamedQuery("EndUser.hasRating")
                .setParameter("player", player)
                .setParameter("game", game)
                .getSingleResult()).intValue() > 0){
            return true;
        }
        return false;
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRating").executeUpdate();
    }
}
