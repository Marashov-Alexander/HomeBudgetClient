package utils;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;

public class ScreenManager {

    private final HashMap<String, Parent> screenMap;
    private final Scene main;

    public ScreenManager(final Scene main, final String mainName) {
        this.main = main;
        this.screenMap = new HashMap<>();
        this.screenMap.put(mainName, main.getRoot());
    }

    public void addScreen(String name, Parent pane) {
        screenMap.put(name, pane);
    }

    public void removeScreen(String name) {
        screenMap.remove(name);
    }

    public void activate(String name) {
        main.setRoot(screenMap.get(name));
    }
}
