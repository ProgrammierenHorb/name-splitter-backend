package com.example.namesplitter.api;

import com.example.namesplitter.model.APIResponse;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.model.TitleDTO;
import com.example.namesplitter.model.TitleData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api")
public interface APIService {

    @PostMapping("/parse")
    public APIResponse parse(@RequestBody String name);

    @GetMapping("/getTitles")
    public List<TitleData> getTitles();

    @PostMapping("/addTitle")
    public boolean addTitle(@RequestBody TitleDTO titleDTO);

    @PostMapping("/removeTitle")
    public boolean removeTitle(@RequestBody TitleData title);

    @PostMapping("/save")
    public StructuredName save(@RequestBody StructuredName name);

    @GetMapping("/status")
    public String status();
}
