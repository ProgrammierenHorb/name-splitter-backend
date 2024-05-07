package com.example.namesplitter.parser;

import com.example.namesplitter.model.ReturnValueAndRemainigString;

public interface ISubParser<T> {
    ReturnValueAndRemainigString<T> parse(String input);
}
