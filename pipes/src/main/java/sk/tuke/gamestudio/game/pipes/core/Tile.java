package sk.tuke.gamestudio.game.pipes.core;

public class Tile {

    private TileDirection tileDirection;
    private boolean isNeededToWin;

    public Tile(TileDirection tileDirection){
        this.tileDirection = tileDirection;
    }

    public TileDirection getTileDirection(){
        return tileDirection;
    }

    public void setIsNeededToWin(boolean isNeededToWin){
        this.isNeededToWin = isNeededToWin;
    }
    public boolean getIsNeededToWin(){
        return isNeededToWin;
    }

    public TileDirection.Group getTileGroup(){
        if(tileDirection.isInGroup(TileDirection.Group.emptyTile))
            return TileDirection.Group.emptyTile;
        else if(tileDirection.isInGroup(TileDirection.Group.rightAngledPipe))
            return TileDirection.Group.rightAngledPipe;
        else if(tileDirection.isInGroup(TileDirection.Group.straightPipe))
            return TileDirection.Group.straightPipe;
        else if(tileDirection.isInGroup(TileDirection.Group.triplePipe))
            return TileDirection.Group.triplePipe;
        else
            return TileDirection.Group.startEndTile;
    }

    void setTileDirection(TileDirection tile){
        this.tileDirection = tile;
    }

}
