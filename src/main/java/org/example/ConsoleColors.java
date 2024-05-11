package org.example;

public final class ConsoleColors {
    private static final String RESET = "\033[0m";  // Text Reset
    private static final String BLACK = "\033[0;30m";   // BLACK
    private static final String RED = "\033[0;31m";     // RED
    private static final String GREEN = "\033[0;32m";   // GREEN
    private static final String YELLOW = "\033[0;33m";  // YELLOW
    private static final String BLUE = "\033[0;34m";    // BLUE
    private static final String PURPLE = "\033[0;35m";  // PURPLE
    private static final String CYAN = "\033[0;36m";    // CYAN
    private static final String WHITE = "\033[0;37m";   // WHITE
    private static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    private static final String RED_BOLD = "\033[1;31m";    // RED
    private static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    private static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    private static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    private static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    private static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    private static final String WHITE_BOLD = "\033[1;37m";  // WHITE
    private static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    private static final String RED_UNDERLINED = "\033[4;31m";    // RED
    private static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    private static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    private static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    private static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    private static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    private static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE
    private static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    private static final String RED_BACKGROUND = "\033[41m";    // RED
    private static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    private static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    private static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    private static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    private static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    private static final String WHITE_BACKGROUND = "\033[47m";  // WHITE
    private static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    private static final String RED_BRIGHT = "\033[0;91m";    // RED
    private static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    private static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    private static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    private static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    private static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    private static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE
    private static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    private static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    private static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    private static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    private static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    private static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    private static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    private static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE
    private static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    private static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    private static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    private static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    private static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    private static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    private static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    private static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE


    @SuppressWarnings("DuplicatedCode")
    public static String colorize(String string, ColorValue color, ColorType type) {
        return switch (color) {
            case BLACK -> switch (type) {
                case DEFAULT -> BLACK;
                case BRIGHT -> BLACK_BRIGHT;
                case UNDERLINE -> BLACK_UNDERLINED;
                case BOLD -> BLACK_BOLD;
                case BACKGROUND -> BLACK_BACKGROUND;
                case BOLD_BRIGHT -> BLACK_BOLD_BRIGHT;
                case BACKGROUND_BRIGHT -> BLACK_BACKGROUND_BRIGHT;
            };
            case RED -> switch (type) {
                case DEFAULT -> RED;
                case BRIGHT -> RED_BRIGHT;
                case UNDERLINE -> RED_UNDERLINED;
                case BOLD -> RED_BOLD;
                case BACKGROUND -> RED_BACKGROUND;
                case BOLD_BRIGHT -> RED_BOLD_BRIGHT;
                case BACKGROUND_BRIGHT -> RED_BACKGROUND_BRIGHT;
            };
            case GREEN -> switch (type) {
                case DEFAULT -> GREEN;
                case BRIGHT -> GREEN_BRIGHT;
                case UNDERLINE -> GREEN_UNDERLINED;
                case BOLD -> GREEN_BOLD;
                case BACKGROUND -> GREEN_BACKGROUND;
                case BOLD_BRIGHT -> GREEN_BOLD_BRIGHT;
                case BACKGROUND_BRIGHT -> GREEN_BACKGROUND_BRIGHT;
            };
            case YELLOW -> switch (type) {
                case DEFAULT -> YELLOW;
                case BRIGHT -> YELLOW_BRIGHT;
                case UNDERLINE -> YELLOW_UNDERLINED;
                case BOLD -> YELLOW_BOLD;
                case BACKGROUND -> YELLOW_BACKGROUND;
                case BOLD_BRIGHT -> YELLOW_BOLD_BRIGHT;
                case BACKGROUND_BRIGHT -> YELLOW_BACKGROUND_BRIGHT;
            };
            case BLUE -> switch (type) {
                case DEFAULT -> BLUE;
                case BRIGHT -> BLUE_BRIGHT;
                case UNDERLINE -> BLUE_UNDERLINED;
                case BOLD -> BLUE_BOLD;
                case BACKGROUND -> BLUE_BACKGROUND;
                case BOLD_BRIGHT -> BLUE_BOLD_BRIGHT;
                case BACKGROUND_BRIGHT -> BLUE_BACKGROUND_BRIGHT;
            };
            case PURPLE -> switch (type) {
                case DEFAULT -> PURPLE;
                case BRIGHT -> PURPLE_BRIGHT;
                case UNDERLINE -> PURPLE_UNDERLINED;
                case BOLD -> PURPLE_BOLD;
                case BACKGROUND -> PURPLE_BACKGROUND;
                case BOLD_BRIGHT -> PURPLE_BOLD_BRIGHT;
                case BACKGROUND_BRIGHT -> PURPLE_BACKGROUND_BRIGHT;
            };
            case CYAN -> switch (type) {
                case DEFAULT -> CYAN;
                case BRIGHT -> CYAN_BRIGHT;
                case UNDERLINE -> CYAN_UNDERLINED;
                case BOLD -> CYAN_BOLD;
                case BACKGROUND -> CYAN_BACKGROUND;
                case BOLD_BRIGHT -> CYAN_BOLD_BRIGHT;
                case BACKGROUND_BRIGHT -> CYAN_BACKGROUND_BRIGHT;
            };
            case WHITE -> switch (type) {
                case DEFAULT -> WHITE;
                case BRIGHT -> WHITE_BRIGHT;
                case UNDERLINE -> WHITE_UNDERLINED;
                case BOLD -> WHITE_BOLD;
                case BACKGROUND -> WHITE_BACKGROUND;
                case BOLD_BRIGHT -> WHITE_BOLD_BRIGHT;
                case BACKGROUND_BRIGHT -> WHITE_BACKGROUND_BRIGHT;
            };
        } + string + RESET;
    }

    public enum ColorValue {
        BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE
    }

    public enum ColorType {
        DEFAULT, BRIGHT, BOLD, BOLD_BRIGHT, UNDERLINE, BACKGROUND, BACKGROUND_BRIGHT
    }
}
