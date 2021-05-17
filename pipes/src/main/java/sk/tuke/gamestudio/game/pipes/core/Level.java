package sk.tuke.gamestudio.game.pipes.core;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Level {

    private int rowCount, columnCount, startPosition, endPosition, levelsCount,
            pipesCount, pipesCountToWin, movesCount;
    private Tile[][] tilesToWin;
    private Tile[][] tiles;
    private JSONObject level;
    private JSONObject level1;
    private TileDirection alternativeExpected;



    public Level(int level){

        this.tiles = new Tile[rowCount][columnCount];
        this.tilesToWin = new Tile[rowCount][columnCount];
        generateLevel(level);

    }

    private void generateLevel(int level) {

        loadJSON(level);
        generateStartEndTile(startPosition, endPosition);
        generateEmptyTiles(tilesToWin);
        generateEmptyTiles(tiles);

    }

    private void loadJSON(int numberOfLevel){
        String filePath = new File("").getAbsolutePath();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(filePath + "/pipes/src/main/java/sk/tuke/gamestudio/game/pipes/core/Levels/Levels.json"));
            JSONObject jsonObject = (JSONObject) obj;

            //get level parameters
            JSONObject levelParameters = (JSONObject) jsonObject.get("Levels");
            levelsCount = Integer.parseInt(levelParameters.get("levelsCount").toString());

            //get levels object
            level = (JSONObject) jsonObject.get("levels");

            switch (numberOfLevel){
                case 1 -> parseSelectedLevel(1);
                case 2 -> parseSelectedLevel(2);
                case 3 -> parseSelectedLevel(3);
                case 4 -> parseSelectedLevel(4);
                case 5 -> parseSelectedLevel(5);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseSelectedLevel(int numberOfLevel){
        String levelIterator = String.valueOf(numberOfLevel);
        level1 = (JSONObject) level.get(levelIterator);

        JSONArray startingPositionArray = (JSONArray) level1.get("startEnd");
        startPosition = Integer.parseInt(startingPositionArray.get(0).toString());
        endPosition = Integer.parseInt(startingPositionArray.get(1).toString());
        rowCount = Integer.parseInt(level1.get("rowCount").toString());
        columnCount = Integer.parseInt(level1.get("columnCount").toString());
        pipesCount = Integer.parseInt(level1.get("pipesCount").toString());
        pipesCountToWin = Integer.parseInt(level1.get("pipesCountToWin").toString());
        movesCount = Integer.parseInt(level1.get("moves").toString());

        this.tiles = new Tile[rowCount][columnCount];
        this.tilesToWin = new Tile[rowCount][columnCount];

        parsePipesOfLevel();
    }

    private void parsePipesOfLevel(){
        JSONObject pipes = (JSONObject) level1.get("pipes");
        String pipesIterator;

        for(int i = 1; i < pipesCount + 1; i++ ){
            pipesIterator = String.valueOf(i);
            JSONObject pipeInfo = (JSONObject) pipes.get(pipesIterator);

            int row = Integer.parseInt(pipeInfo.get("row").toString());
            int column = Integer.parseInt(pipeInfo.get("column").toString());

            Boolean isNeededToWin = (Boolean) pipeInfo.get("isNeededToWin");
            TileDirection.Group group = TileDirection.Group.valueOf(pipeInfo.get("tileGroup").toString());
            JSONObject tileDirectionObj = (JSONObject) pipeInfo.get("tileDirection");
            TileDirection tileDirectionToWin = TileDirection.valueOf(tileDirectionObj.get("expected").toString());

            if(group == TileDirection.Group.triplePipe)
                alternativeExpected = TileDirection.valueOf(tileDirectionObj.get("alternativeExpected").toString());

            TileDirection initialTD = TileDirection.valueOf(tileDirectionObj.get("initial").toString());
            generateTilePipe(row, column, tileDirectionToWin, initialTD, isNeededToWin);
        }
    }

    private void generateTilePipe(int row, int column, TileDirection expectedTD, TileDirection initialTD, boolean isNeededToWin) {
        tilesToWin[row][column] = new TilePipe(expectedTD);
        tilesToWin[row][column].setIsNeededToWin(isNeededToWin);
        tiles[row][column] = new TilePipe(initialTD);
        tiles[row][column].setIsNeededToWin(isNeededToWin);
    }

    private void generateStartEndTile(int startPosition, int endPosition){
        tiles[startPosition][0] = new TileStartEnd(TileDirection.START);
        tiles[endPosition][columnCount - 1] = new TileStartEnd(TileDirection.END);
        tilesToWin[startPosition][0] = new TileStartEnd(TileDirection.START);
        tilesToWin[endPosition][columnCount - 1] = new TileStartEnd(TileDirection.END);
    }

    private void generateEmptyTiles(Tile[][] tile) {
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (tile[row][column] == null)
                    tile[row][column] = new TileEmpty(TileDirection.EMPTY);
            }
        }
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    public Tile getTileToWin(int row, int column) {
        return tilesToWin[row][column];
    }

    public int getPipesCountToWin(){
        return pipesCountToWin;
    }

    public int getRowCount(){
        return rowCount;
    }

    public int getColumnCount(){
        return columnCount;
    }

    public int getMovesCount(){
        return movesCount;
    }

    public boolean isNeededToWin(int row, int column) {
        return getTileToWin(row, column).getIsNeededToWin();
    }

    public int getLevelsCount() {
        return levelsCount;
    }

    public TileDirection getAlternativeExpected() {
        return alternativeExpected;
    }
}
