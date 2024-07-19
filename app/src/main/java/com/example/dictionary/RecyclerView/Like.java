package com.example.dictionary.RecyclerView;

public class Like {
    String word;
    String part;
    String translation;
    String ttsUrl;
    private String firstLetter;
    private boolean isChecked;
    public Like (String word,String translation,String ttsUrl){
        this.word = word;
        this.translation = translation;
        this.ttsUrl = ttsUrl;
        this.firstLetter = Character.toString(word.charAt(0)).toUpperCase();
        this.isChecked = false;
    }

    public String getPart() {
        return part;
    }

    public String getTranslation() {
        return translation;
    }

    public String getTtsUrl() {
        return ttsUrl;
    }

    public String getWord() {
        return word;
    }

    public String getFirstLetter() {
        return firstLetter;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
