package com.example.mongrammaire;

public class DictionaryWord {
    private String word;
    private String definition;
    private String partOfSpeech;

    public DictionaryWord(String word, String definition, String partOfSpeech) {
        this.word = word;
        this.definition = definition;
        this.partOfSpeech = partOfSpeech;
    }

    public String getWord() { return word; }
    public String getDefinition() { return definition; }
    public String getPartOfSpeech() { return partOfSpeech; }
}
