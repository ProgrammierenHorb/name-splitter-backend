package com.example.namesplitter.model;

public record TitleData(String regex, String name, Gender gender, int priority) implements Comparable<TitleData> {

    public TitleData(TitleData otherTitle, String regex){
        this(regex, otherTitle.name(), otherTitle.gender(), otherTitle.priority());
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o instanceof TitleData t){
            return this.name.equals(t.name);
        }

        return false;
    }

    @Override
    public int compareTo(TitleData other) {
        return Integer.compare(this.priority, other.priority);
    }
}
