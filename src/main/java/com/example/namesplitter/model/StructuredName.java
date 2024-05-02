package com.example.namesplitter.model;

import java.util.List;

public record StructuredName(Gender gender, List<String> titles, String firstName, String lastName, String standardizedSalutation){
}
