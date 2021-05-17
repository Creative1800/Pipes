package sk.tuke.gamestudio.game.pipes.core;

import org.junit.Test;
import sk.tuke.gamestudio.game.pipes.core.Field;
import sk.tuke.gamestudio.game.pipes.core.GameState;
import sk.tuke.gamestudio.game.pipes.core.TileDirection;

import static org.junit.Assert.assertEquals;

public class FieldTest {

    private final Field field;

    public FieldTest() {

        field = new Field(1);
        while (field.getTile(0, 1).getTileDirection() != TileDirection.SOUTHWEST)
            field.turnPipe(0, 1, TileDirection.Group.rightAngledPipe);
        while (field.getTile(1, 1).getTileDirection() != TileDirection.NORTHEAST)
            field.turnPipe(1, 1, TileDirection.Group.rightAngledPipe);
        while (field.getTile(1, 2).getTileDirection() != TileDirection.EASTWEST)
            field.turnPipe(1, 2, TileDirection.Group.straightPipe);
        while (field.getTile(1, 3).getTileDirection() != TileDirection.SOUTHWEST)
            field.turnPipe(1, 3, TileDirection.Group.rightAngledPipe);
        while (field.getTile(2, 3).getTileDirection() != TileDirection.NORTHEASTSOUTH)
            field.turnPipe(2, 3, TileDirection.Group.triplePipe);
    }

    @Test
    public void movesTest() {
        assertEquals(field.getMoves(), 8);
    }

    @Test
    public void scoreTest() {
        assertEquals(field.getScore(), 55);
    }

    @Test
    public void gameStateTest() {
        assertEquals(field.getGameState(), GameState.SOLVED);
    }
}













