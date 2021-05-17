package sk.tuke.gamestudio.game.pipes.core;


public class Field {

    private final int rowCount;
    private final int columnCount;
    private final Level level;
    private final int levelNumber;
    private int moves;
    private GameState state = GameState.PLAYING;

    public Field(int levelNumber) {
        level = new Level(levelNumber);

        this.levelNumber = levelNumber;
        this.rowCount = level.getRowCount();
        this.columnCount = level.getColumnCount();
        this.moves = level.getMovesCount();
    }

    public void turnPipe(int row, int column, TileDirection.Group group) {
        Tile tile = level.getTile(row, column);

        if (row > rowCount || column > columnCount)
            return;

        if (group != TileDirection.Group.emptyTile)
            moves--;

        switch (group) {
            case rightAngledPipe -> turnRightAngled(tile);
            case straightPipe -> turnStraight(tile);
            case triplePipe -> turnTriple(tile);
            case emptyTile -> throw new IllegalArgumentException("Cannot move empty tile!");
        }

        checkGameState();
    }

    private void turnRightAngled(Tile tile) {
        switch (tile.getTileDirection()) {
            case NORTHEAST -> tile.setTileDirection(TileDirection.SOUTHEAST);
            case SOUTHEAST -> tile.setTileDirection(TileDirection.SOUTHWEST);
            case SOUTHWEST -> tile.setTileDirection(TileDirection.NORTHWEST);
            case NORTHWEST -> tile.setTileDirection(TileDirection.NORTHEAST);
        }
    }

    private void turnStraight(Tile tile) {
        switch (tile.getTileDirection()) {
            case NORTHSOUTH -> tile.setTileDirection(TileDirection.EASTWEST);
            case EASTWEST -> tile.setTileDirection(TileDirection.NORTHSOUTH);
        }
    }

    private void turnTriple(Tile tile) {
        switch (tile.getTileDirection()) {
            case WESTNORTHEAST -> tile.setTileDirection(TileDirection.NORTHEASTSOUTH);
            case NORTHEASTSOUTH -> tile.setTileDirection(TileDirection.EASTSOUTHWEST);
            case EASTSOUTHWEST -> tile.setTileDirection(TileDirection.SOUTHWESTNORTH);
            case SOUTHWESTNORTH -> tile.setTileDirection(TileDirection.WESTNORTHEAST);
        }
    }

    private void checkGameState() {
        if (isGameWon())
            state = GameState.SOLVED;
        else if (moves == 0)
            state = GameState.FAILED;

    }

    private boolean isGameWon() {
        int pipeCounter = 0;
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (level.getTile(row, column) instanceof TilePipe
                        && level.getTile(row, column).getTileDirection() == level.getTileToWin(row, column).getTileDirection()
                        && level.isNeededToWin(row, column)
                        || level.getTile(row, column).getTileDirection() == level.getAlternativeExpected()) {
                    pipeCounter++;
                }
            }
        }
        return pipeCounter == level.getPipesCountToWin();
    }

    public int getMoves() {
        return moves;
    }

    public Tile getTile(int row, int column) {
        return level.getTile(row, column);
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public GameState getGameState() {
        return state;
    }

    public int getScore() {
        return rowCount * columnCount + (moves * 5 * levelNumber);
    }

    public int getLevelsCount() {
        return level.getLevelsCount();
    }

    public int getLevelNumber() {
        return levelNumber;
    }
}
