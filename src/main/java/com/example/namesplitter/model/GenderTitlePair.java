package com.example.namesplitter.model;

public record GenderTitlePair(Gender g, String t) {
    Gender getGender(){
        return g;
    }

    String getTitle(){
        return t;
    }
}
