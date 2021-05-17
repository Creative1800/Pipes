package sk.tuke.gamestudio.game.pipes.service;

import sk.tuke.gamestudio.game.pipes.entity.Rating;

import java.sql.*;


public class RatingServiceJDBC implements RatingService {

    public static final String SELECT = "SELECT game, player, rating, ratedOn FROM rating WHERE game = ?";
    public static final String SELECTAVG = "SELECT rating FROM rating WHERE game = ?";
    public static final String DELETE = "DELETE FROM rating";
    public static final String INSERT =
            "INSERT INTO rating (game, player, rating, ratedOn) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON CONFLICT (game, player) " +
                    "WHERE game = ? " +
                    "DO UPDATE SET rating = EXCLUDED.rating;";
    public static final String HASRATING = "SELECT COUNT(player) FROM Rating WHERE player = ? AND game = ?";


    @Override
    public void setRating(Rating rating) throws sk.tuke.gamestudio.game.pipes.service.RatingException {
        try (Connection connection = DriverManager.getConnection(ServerUUP.URL, ServerUUP.USER, ServerUUP.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getPlayer());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statement.setString(5, rating.getGame());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new sk.tuke.gamestudio.game.pipes.service.RatingException("Problem inserting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws sk.tuke.gamestudio.game.pipes.service.RatingException {
        try (Connection connection = DriverManager.getConnection(ServerUUP.URL, ServerUUP.USER, ServerUUP.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECTAVG)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                double avg = 0;
                double count = 0;
                while (rs.next()) {
                    avg = avg + rs.getInt(1);
                    count++;
                }
                double result = (avg / count) + 0.5;
                return (int) result;
            }
        } catch (SQLException e) {
            throw new sk.tuke.gamestudio.game.pipes.service.CommentException("Problem selecting rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws sk.tuke.gamestudio.game.pipes.service.RatingException {
        try (Connection connection = DriverManager.getConnection(ServerUUP.URL, ServerUUP.USER, ServerUUP.PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    if (game.equals(rs.getString(1)) && player.equals(rs.getString(2))) {
                        return rs.getInt(3);
                    }
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new CommentException("Problem getting rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(ServerUUP.URL, ServerUUP.USER, ServerUUP.PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting rating", e);
        }
    }

    @Override
    public boolean hasRating(String player, String game) throws UserException {
        return false;
    }
}
