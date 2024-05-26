package org.example.machinery;

import org.example.ConsoleColors;

public enum Team {
    WHITE, BLACK;
    
    public ConsoleColors.ColorValue toColor() {
        return switch (this) {
            case WHITE -> ConsoleColors.ColorValue.WHITE;
            case BLACK -> ConsoleColors.ColorValue.BLACK;
        };
    }
    public Team invert(){
        return switch (this) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
        };
    }
}
