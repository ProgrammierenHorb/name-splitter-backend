package com.example.namesplitter.api;

import com.example.namesplitter.model.APIResponse;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.model.TitleDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api")
public interface APIService {

    @GetMapping("/parse/{name}")
    public APIResponse parse(@PathVariable String name);

    @GetMapping("/getTitles")
    public List<String> getTitles();

    @PostMapping("/addTitle")
    public boolean addTitle(@RequestBody TitleDTO titleDTO);

    @PostMapping("/removeTitle")
    public boolean removeTitle(@RequestBody String title);

    @PostMapping("/save")
    public StructuredName save(@RequestBody StructuredName name);

    @GetMapping("/status")
    public String status();
}
