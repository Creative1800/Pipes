package sk.tuke.gamestudio.game.pipes.service;

import sk.tuke.gamestudio.game.pipes.entity.Score;

import java.util.List;


public interface ScoreService {

    void addScore(Score score) throws ScoreException;

    List<Score> getTopScores(String game) throws ScoreException;

    void reset() throws ScoreException;
}
