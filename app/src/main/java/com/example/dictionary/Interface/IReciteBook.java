package com.example.dictionary.Interface;

public interface IReciteBook {
    public interface VP{
        public void requestNetworkData(String apiUrl,int number);
        public void responseForFetch(String word, String symbol, String desc, String soundUrl);
    }
    public interface M{
        public void requestNetworkData(String apiUrl,int number);
    }
}
