package org.example;

public enum Team {
    WHITE, BLACK;

    public ConsoleColors.ColorValue toColor() {
        return switch (this) {
            case WHITE -> ConsoleColors.ColorValue.WHITE;
            case BLACK -> ConsoleColors.ColorValue.BLACK;
        };
    }
}
