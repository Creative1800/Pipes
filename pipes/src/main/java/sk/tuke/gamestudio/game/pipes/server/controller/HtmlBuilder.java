package sk.tuke.gamestudio.game.pipes.server.controller;

import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.game.pipes.core.Field;
import sk.tuke.gamestudio.game.pipes.core.GameState;
import sk.tuke.gamestudio.game.pipes.core.Tile;
import sk.tuke.gamestudio.game.pipes.core.TileDirection;
import sk.tuke.gamestudio.game.pipes.service.RatingService;

public class HtmlBuilder {

    public void getGameStats(StringBuilder sb, Field field){
        sb.append("<div class='gameInfo'>");
        sb.append("<div class='levelMoves'>");
        sb.append("<div class='levelNumber'>\n");
        sb.append(String.format("<p class='levelInfo'>Level: <span class='levelInfoSpan'>%d</span></p>\n", field.getLevelNumber()));
        sb.append("</div>\n");
        sb.append("<div class='movesCounter'>\n");
        sb.append(String.format("<p class='levelInfo'>Moves: <span class='levelInfoSpan'>%d</span></p>\n", field.getMoves()));
        sb.append("</div>\n");
        sb.append("</div>");
        sb.append("<div class='menuOrAgain'>");
        sb.append("<div class='tryAgain'>");
        sb.append("<a href='/pipes/new'>");
        sb.append("<img class='tryAgainImg' src='/images/tryAgain1.png'>");
        sb.append("</a>");
        sb.append("</div>");
        sb.append("<div class='menu'>");
        sb.append("<a href='/pipes/menu'>");
        sb.append("<img class='menuImg' src='/images/menu.jpg'>");
        sb.append("</a>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
    }

    public void getGameBackground(StringBuilder sb, Field field) {
        sb.append("<div class='gameBackground'>\n");
        getGameStats(sb, field);
        sb.append("<div class='mainTableSection'>");
        sb.append("<table class='mainTable'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);

                sb.append("<td>\n");
                if (tile.getTileGroup() != TileDirection.Group.startEndTile &&
                        tile.getTileGroup() != TileDirection.Group.emptyTile &&
                        field.getGameState() == GameState.PLAYING) {
                    sb.append(String.format("<a href='/pipes?row=%d&column=%d&group=%s'>", row, column, tile.getTileGroup()));
                }
                sb.append("<img class='pipes-img' src='/images/pipes/pipes/" + getImageName(tile) + ".png'>");
                if (tile.getTileGroup() != TileDirection.Group.startEndTile &&
                        tile.getTileGroup() != TileDirection.Group.emptyTile &&
                        field.getGameState() == GameState.PLAYING) {
                    sb.append("</a>");
                }
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");

    }

    private String getImageName(Tile tile) {
        switch (tile.getTileGroup()) {
            case emptyTile -> {
                return printEmptyTile();
            }
            case straightPipe -> {
                return printStraightPipe(tile);
            }
            case rightAngledPipe -> {
                return printRightAngledPipe(tile);
            }
            case triplePipe -> {
                return printTriplePipe(tile);
            }
            case startEndTile -> {
                return printStartEndTile(tile);
            }
            default -> throw new IllegalArgumentException("Unsupported tile state");
        }
    }

    private String printEmptyTile() {
        return "emptyPipe";
    }

    private String printRightAngledPipe(@NotNull Tile tile) {
        switch (tile.getTileDirection()) {
            case NORTHEAST -> {
                return "NE";
            }
            case SOUTHEAST -> {
                return "SE";
            }
            case SOUTHWEST -> {
                return "SW";
            }
            case NORTHWEST -> {
                return "NW";
            }
            default -> throw new IllegalArgumentException("Unsupported tile state");
        }
    }

    private String printStraightPipe(Tile tile) {
        switch (tile.getTileDirection()) {
            case NORTHSOUTH -> {
                return "NS";
            }
            case EASTWEST -> {
                return "EW";
            }
            default -> throw new IllegalArgumentException("Unsupported tile state");
        }
    }

    private String printTriplePipe(Tile tile) {
        switch (tile.getTileDirection()) {
            case WESTNORTHEAST -> {
                return "WNE";
            }
            case NORTHEASTSOUTH -> {
                return "NES";
            }
            case EASTSOUTHWEST -> {
                return "ESW";
            }
            case SOUTHWESTNORTH -> {
                return "SWN";
            }
            default -> throw new IllegalArgumentException("Unsupported tile state");
        }
    }

    private String printStartEndTile(Tile tile) {
        switch (tile.getTileDirection()) {
            case START -> {
                return "StartPipe";
            }
            case END -> {
                return "EndPipe";
            }
            default -> throw new IllegalArgumentException("Unsupported tile state");
        }
    }

    public void gameHomePage(StringBuilder sb) {
        sb.append("<div class='gameBackground'>\n");
        sb.append("<div class='playBtn'>");
        sb.append("<a class='play-btn' href='/pipes'></a>");
        sb.append("</div>\n");
        sb.append("</div>\n");

    }

    public void showMenu(StringBuilder sb, Field field, RatingService ratingService) {
        sb.append("<div class='gameBackground'>\n");
        sb.append("<div class='levelSection'>");
        sb.append("<div class='homeBtnSection'>");
        sb.append("<a href='/pipes/home'><img class='homeImg' src='/images/home.png'></a>\n");
        sb.append("<div class='avgRating'>");
        sb.append("<h2 class='gameRating'>Game Rating</h2>");
        for (int i = 0; i < 5 ; i++){
            if (i < ratingService.getAverageRating("Pipes"))
                sb.append(String.format("<span class='avgStar filledStar'>★</span>"));
            else
                sb.append(String.format("<span class='avgStar emptyStar'>★</span>"));
        }
        sb.append("</div>\n");
        sb.append("</div>\n");
        sb.append("<div class='levelH1Section'>");
        sb.append("<h1 class='levelH1'>Levels</h1>");
        for (int i = 1; i < field.getLevelsCount() + 1; i++){
            sb.append(String.format("<a href='/pipes/new?level=%d' class='levelNum'>%d</a>", i, i));
        }
        sb.append("</div>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
    }

    public void nextLevelForm(StringBuilder sb, Field field) {
        sb.append("<div class='gameBackground'>\n");
        sb.append("<div class='nextLevel'>");
        sb.append("<div class='nextLevelH1'>");
        if(field.getGameState() == GameState.SOLVED)
            sb.append("<h2 class='youWon'>You Won!</h2>");
        if(field.getGameState() == GameState.FAILED)
            sb.append("<h2 class='youWon'>Out of Moves!</h2>");
        sb.append("</div>");
        sb.append("<div class='scoreAfterLevel'>");
        if(field.getGameState() == GameState.SOLVED)
            sb.append(String.format("<span class='levelScore' >Level Score:<span class='levelScoreNum'> %d</span></span>", field.getScore()));
        if(field.getGameState() == GameState.FAILED)
            sb.append("<span class='levelScore' >Level Score:<span class='levelScoreNum'> 0</span></span>");
        sb.append("</div>");
        sb.append("<div class='nextLevelButtons'>");
        sb.append("<a href=\"/pipes/new\" ><img class='nextlevelImg' src='/images/tryAgain1.png'></a>");
        if(field.getLevelNumber() != field.getLevelsCount() && field.getGameState() == GameState.SOLVED)
            sb.append("<a href=\"/pipes/nextLevel\"><img class='nextlevelImg' src='/images/nextLevelButton.png'></a>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
    }
}
