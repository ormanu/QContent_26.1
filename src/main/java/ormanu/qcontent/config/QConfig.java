package ormanu.qcontent.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class QConfig extends MidnightConfig {

    @Entry(category = "movement") public static double fireworkRecoilPower = 1.5;
    @Entry(category = "movement") public static double fireworkUpwardKick = 0.5;
    @Entry(category = "movement") public static int airBoostDuration = 100;
    @Entry(category = "movement") public static double airAcceleration = 0.09;
    @Entry(category = "movement") public static double maxAirSpeed = 2.5;

}
