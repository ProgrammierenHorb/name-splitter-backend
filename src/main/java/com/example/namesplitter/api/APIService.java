package com.example.namesplitter.api;

import com.example.namesplitter.model.APIResponse;
import com.example.namesplitter.model.StructuredName;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping("api")
public interface APIService {

    @GetMapping("/parse/{name}")
    public APIResponse parse(@PathVariable String name);

    @GetMapping("/getTitles")
    public List<String> getTitles();

    @PostMapping("/addTitle")
    public void addTitle(@PathVariable String title);

    @PostMapping("/save")
    public void save(@PathVariable StructuredName name);

    @GetMapping("/status")
    public String status();
}
