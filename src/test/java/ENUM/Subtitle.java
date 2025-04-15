package ENUM;


import java.text.Normalizer;

public enum Subtitle {
    SUBTITLE("E");

    private final String text;

    Subtitle(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
