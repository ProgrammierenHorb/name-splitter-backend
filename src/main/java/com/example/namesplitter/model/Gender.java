package com.example.namesplitter.model;

public enum Gender {
    MALE("Herr"),
    FEMALE("Frau"),
    DIVERSE("");

    final String salutation;

    Gender(String salutation){
        this.salutation = salutation;

    }
}
