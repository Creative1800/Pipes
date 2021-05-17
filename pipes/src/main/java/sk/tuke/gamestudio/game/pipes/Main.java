package sk.tuke.gamestudio.game.pipes;

import sk.tuke.gamestudio.game.pipes.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.pipes.core.Field;

public class Main {
    public static void main(String[] args) {

        Field field = new Field(1);

        ConsoleUI ui = new ConsoleUI(field);

        ui.play();
    }

}
