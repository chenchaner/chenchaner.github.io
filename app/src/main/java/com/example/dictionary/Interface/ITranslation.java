package com.example.dictionary.Interface;

public interface ITranslation {
    public interface VP{
        public void requestNetwork(String url,String inputText);
        public void responseForNetwork(String translation);

    }
    public interface M{
        public void requestNetwork(String url,String inputText);
    }
}
