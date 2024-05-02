package com.example.namesplitter.api;

import com.example.namesplitter.model.APIResponse;
import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.parser.NameParser;
import com.example.namesplitter.parser.Parsable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTService implements APIService{


    @Override
    public APIResponse parse(String name) {
        Parsable parser = new NameParser();
        return new APIResponse(false, "", parser.parse(name));
    }
}
