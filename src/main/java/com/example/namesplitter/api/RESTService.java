package com.example.namesplitter.api;

import com.example.namesplitter.model.APIResponse;
import com.example.namesplitter.model.Gender;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.model.Title;
import com.example.namesplitter.parser.NameParser;
import com.example.namesplitter.parser.Parsable;
import com.example.namesplitter.storage.InMemoryTitleStorage;
import com.example.namesplitter.storage.SQLiteNameGenderService;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RESTService implements APIService{

    public TitleStorageService titleStorageService = new InMemoryTitleStorage();

    @Override
    public APIResponse parse(String name) {
        Parsable parser = new NameParser();
        System.out.println(new SQLiteNameGenderService().getGender(name));
        return new APIResponse(false, "", parser.parse(name));
    }

    @Override
    public List<String> getTitles() {
       return titleStorageService.getAllTitles().values().stream().toList();
    }

    @Override
    public void addTitle(Title title) {
        titleStorageService.addTitle(title.name(), title.regex());
    }

    @Override
    public void save(StructuredName name) {

    }
}
