package com.example.namesplitter.api;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTService implements APIService{

    @Override
    public StructuredName parse(String name) {
        return new StructuredName(Gender.MALE, "Herr Dr.", name, "Test", null);
    }
}
