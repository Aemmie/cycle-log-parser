package ru.aemmie.cycle.objects;

import java.time.Duration;

public enum GameMap {
    BRIGHT_SANDS(Timings.NORMAL),
    CRESCENT_FALLS(Timings.NORMAL),
    THARIS_ISLAND(Timings.THARIS);

    public final Timings timings;

    GameMap(Timings timings) {
        this.timings = timings;
    }

    public static final class Timings {
        private static final Timings NORMAL = new Timings(
                duration(4, 0),
                duration(16, 40),
                duration(13, 20),
                duration(4, 40));
        private static final Timings THARIS = new Timings(
                duration(4, 0),
                duration(12, 40),
                duration(8, 20),
                duration(4, 40));

        public final long timeBetweenStorms;
        public final long morning;
        public final long day;
        public final long evening;
        public final long night;

        private Timings(long morning, long day, long evening, long night) {
            this.timeBetweenStorms = morning + day + evening + night;
            this.morning = morning;
            this.day = day;
            this.evening = evening;
            this.night = night;
        }

        private static long duration(long minutes, long seconds) {
            return Duration.ofMinutes(minutes).plusSeconds(seconds).toMillis();
        }

    }
}
