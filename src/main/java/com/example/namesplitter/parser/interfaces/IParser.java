package com.example.namesplitter.parser.interfaces;

import com.example.namesplitter.exception.NameSplitterException;
import com.example.namesplitter.model.StructuredName;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface IParser {
    Pair<StructuredName, List<? extends NameSplitterException>> parse (String input);
}
