package com.example.dictionary.Interface;

public interface IDailySaying {
    public interface VP{
        void requestNetwork(String apiUrl);
        void setImageUrl(String imageUrl);
        void setTextSentence(String TextSentence);
        void setTextTranslation(String TextTranslation);
        void setTextDate(String TextDate);
        void setTtsUrl(String ttsUrl);
    }


    public interface M{
        void requestNetwork(String apiUrl);
    }
}
