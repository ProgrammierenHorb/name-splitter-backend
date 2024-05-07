package com.example.namesplitter.parser;

import com.example.namesplitter.exception.NameSplitterException;
import com.example.namesplitter.model.StructuredName;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface Parser {
    Pair<StructuredName, List<? extends NameSplitterException>> parse (String input);
}
