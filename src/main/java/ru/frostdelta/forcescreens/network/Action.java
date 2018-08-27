package ru.frostdelta.forcescreens.network;

import java.util.HashMap;

public enum Action {
    SCREENSHOT("Screenshot"), SCREENSHOTS("Screenshots"), UNKNOWN("Unknown action");

    private static final HashMap<String, Action> actions = new HashMap<>();

    static {
        for (Action ac : Action.values()) {
            actions.put(ac.action, ac);
        }
    }

    private final String action;

     Action(String action) {
        this.action = action;
    }

    public String getActionName() {
        return action;
    }

    public static Action getAction(String name) {
        return actions.getOrDefault(name, Action.UNKNOWN);
    }

    public static boolean contains(String name) {
        return actions.containsKey(name);
    }
}