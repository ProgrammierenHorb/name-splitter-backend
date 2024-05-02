package com.example.namesplitter.model;

import java.util.List;

public record StructuredName(Gender gender, List<String> title, String firstName, String lastName, String standardizedSalutation){
}
