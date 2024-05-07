package com.example.namesplitter.model;

public record TitleData(String regex, String name, Gender g, int priority) {
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o instanceof TitleData t){
            return this.regex.equals(t.regex);
        }

        return false;
    }
}
