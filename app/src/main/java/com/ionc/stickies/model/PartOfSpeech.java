package com.ionc.stickies.model;

public enum PartOfSpeech {
    Noun,
    Verb,
    Adjective,
    Adverb,
    Phrase;

    public static PartOfSpeech convertToPOS(String pos) {
        switch (pos) {
            case "Noun":
                return Noun;
            case "Verb":
                return Verb;
            case "Adverb":
                return Adverb;
            case "Adjective":
                return Adjective;
            case "Phrase":
                return Phrase;
            default:
                throw new IllegalArgumentException("Unexpected value: " + pos);
        }
    }
}