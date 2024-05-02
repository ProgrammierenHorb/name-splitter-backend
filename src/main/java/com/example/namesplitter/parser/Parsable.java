package com.example.namesplitter.parser;

import com.example.namesplitter.model.StructuredName;

public interface Parsable {
    StructuredName parse (String input);
}
