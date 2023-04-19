package ru.aemmie.cycle.objects;

import java.util.Random;

public enum Rarity {

    COMMON("#979a9a"),
    UNCOMMON("#58d68d"),
    RARE("#0495b4"),
    EPIC("#b584c8"),
    EXOTIC("#e74c3c"),
    LEGENDARY("orange"),
    RAINBOW("R A I N B O W") {
        @Override
        public String colorize(String text) {
            Rarity[] values = Rarity.values();
            int enumSize = values.length - 1;
            var rngStart = random.nextInt(enumSize);
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                res.append(values[(rngStart + i) % enumSize].colorize(text.substring(i, i + 1)));
            }
            return res.toString();
        }
    };

    public final String color;
    private static final Random random = new Random();

    Rarity(String color) {
        this.color = color;
    }

    public String colorize(String text) {
        return "<font color='%s'>%s</font>".formatted(color, text);
    }
}
