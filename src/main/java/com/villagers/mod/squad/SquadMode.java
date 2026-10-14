package com.villagers.mod.squad;

public enum SquadMode {
    STANDARD,
    RECRUIT;

    public static SquadMode fromString(String raw) {
        if (raw == null) {
            return STANDARD;
        }
        return "recruit".equalsIgnoreCase(raw) ? RECRUIT : STANDARD;
    }
}
