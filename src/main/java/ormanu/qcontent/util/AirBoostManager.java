package ormanu.qcontent.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AirBoostManager {
    // Special value meaning: boost stays active until player lands
    private static final int UNTIL_GROUND = -1;

    private static final Map<UUID, Integer> BOOSTED_PLAYERS = new HashMap<>();

    /** Old behavior: boost for X ticks */
    public static void setBoostTicks(UUID uuid, int ticks) {
        BOOSTED_PLAYERS.put(uuid, ticks);
    }

    /** New behavior: boost until landing */
    public static void setBoostUntilGround(UUID uuid) {
        BOOSTED_PLAYERS.put(uuid, UNTIL_GROUND);
    }

    public static boolean hasBoost(UUID uuid) {
        Integer t = BOOSTED_PLAYERS.get(uuid);
        return t != null && (t == UNTIL_GROUND || t > 0);
    }

    /** Only counts down timed boosts. Does nothing for UNTIL_GROUND boosts. */
    public static void tick(UUID uuid) {
        Integer t = BOOSTED_PLAYERS.get(uuid);
        if (t == null) return;

        if (t == UNTIL_GROUND) return;

        if (t <= 1) BOOSTED_PLAYERS.remove(uuid);
        else BOOSTED_PLAYERS.put(uuid, t - 1);
    }

    public static void clear(UUID uuid) {
        BOOSTED_PLAYERS.remove(uuid);
    }
}