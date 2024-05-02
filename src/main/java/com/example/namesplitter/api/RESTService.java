package com.example.namesplitter.api;

import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.structuredName;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTService implements APIService{

    @Override
    public structuredName parse(String name) {
        return new structuredName(Gender.MALE, "Herr Dr.", name, "Test", null);
    }
}
