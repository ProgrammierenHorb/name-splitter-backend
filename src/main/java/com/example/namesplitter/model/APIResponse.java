package com.example.namesplitter.model;

public record APIResponse(Boolean error, String errorMessage, StructuredName structuredName) {
}
