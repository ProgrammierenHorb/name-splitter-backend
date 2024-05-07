package com.example.namesplitter.model;

import java.util.List;
import java.util.Objects;

public record StructuredName(Gender gender, List<String> titles, String firstName, String lastName, String standardizedSalutation){

    public StructuredName(StructuredName name, String standardizedSalutation){
        this(name.gender(), name.titles(), name.firstName(), name.lastName(), standardizedSalutation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StructuredName that = (StructuredName) o;
        return Objects.equals(gender, that.gender) &&
                titles.equals(that.titles) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName);
    }
}
