package sk.tuke.gamestudio.game.pipes.core;

public enum TileDirection {

    EMPTY (Group.emptyTile),

    START (Group.startEndTile),
    END (Group.startEndTile),

    NORTHSOUTH (Group.straightPipe),
    EASTWEST (Group.straightPipe),

    NORTHEAST (Group.rightAngledPipe),
    SOUTHEAST (Group.rightAngledPipe),
    SOUTHWEST (Group.rightAngledPipe),
    NORTHWEST (Group.rightAngledPipe),

    NORTHEASTSOUTH (Group.triplePipe),
    EASTSOUTHWEST (Group.triplePipe),
    SOUTHWESTNORTH (Group.triplePipe),
    WESTNORTHEAST (Group.triplePipe);

    private final Group group;

    TileDirection(Group group) {
        this.group = group;
    }

    public boolean isInGroup(Group group) {
        return this.group == group;
    }

    public enum Group {
        emptyTile,
        startEndTile,
        rightAngledPipe,
        straightPipe,
        triplePipe,
    }

}
