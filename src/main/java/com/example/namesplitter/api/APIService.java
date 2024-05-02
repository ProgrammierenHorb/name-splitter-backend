package com.example.namesplitter.api;

import com.example.namesplitter.model.APIResponse;
import com.example.namesplitter.model.StructuredName;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api")
public interface APIService {

    @GetMapping("/parse/{name}")
    public APIResponse parse(@PathVariable String name);
}
