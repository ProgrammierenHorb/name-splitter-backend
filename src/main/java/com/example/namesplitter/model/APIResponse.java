package com.example.namesplitter.model;

import java.util.List;

public record APIResponse(List<String> errorMessages, StructuredName structuredName) {
}
