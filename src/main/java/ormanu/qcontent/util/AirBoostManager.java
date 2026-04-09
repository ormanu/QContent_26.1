package ormanu.qcontent.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AirBoostManager {
    private static final Map<UUID, Integer> BOOSTED_PLAYERS = new HashMap<>();

    public static void setBoostTicks(UUID uuid, int ticks) {
        BOOSTED_PLAYERS.put(uuid, ticks);
    }

    public static boolean hasBoost(UUID uuid) {
        return BOOSTED_PLAYERS.getOrDefault(uuid, 0) > 0;
    }

    public static void tick(UUID uuid) {
        int ticks = BOOSTED_PLAYERS.getOrDefault(uuid, 0);
        if (ticks <= 1) {
            BOOSTED_PLAYERS.remove(uuid);
        } else {
            BOOSTED_PLAYERS.put(uuid, ticks - 1);
        }
    }

    public static void clear(UUID uuid) {
        BOOSTED_PLAYERS.remove(uuid);
    }
}