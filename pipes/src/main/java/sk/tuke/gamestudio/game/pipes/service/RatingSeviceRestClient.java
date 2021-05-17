package sk.tuke.gamestudio.game.pipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.game.pipes.entity.Rating;

public class RatingSeviceRestClient implements RatingService{
    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url + "/rating", rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) {
        return restTemplate.getForEntity(url + "rating/avg/" + game, Integer.class).getBody();
    }

    @Override
    public boolean hasRating(String player, String game) throws UserException {
        return false;
    }

    @Override
    public int getRating(String game, String player) {
        return restTemplate.getForEntity(url + "rating/" + game + "/" + player, Integer.class).getBody();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
