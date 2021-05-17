package sk.tuke.gamestudio.game.pipes.service;

import sk.tuke.gamestudio.game.pipes.entity.Rating;

public interface RatingService {

    void setRating(Rating rating) throws RatingException;

    int getAverageRating(String game) throws RatingException;

    boolean hasRating(String player, String game) throws UserException;

    int getRating(String game, String player) throws RatingException;

    void reset() throws RatingException;

}
