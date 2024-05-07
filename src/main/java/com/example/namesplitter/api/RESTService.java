package com.example.namesplitter.api;

import com.example.namesplitter.model.APIResponse;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.model.Title;
import com.example.namesplitter.parser.NameParser;
import com.example.namesplitter.parser.Parser;
import com.example.namesplitter.parser.StandardizedSalutationGenerator;
import com.example.namesplitter.storage.InMemoryTitleStorage;
import com.example.namesplitter.storage.SQLiteNameGenderService;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RESTService implements APIService{

    public TitleStorageService titleStorageService = new InMemoryTitleStorage();

    @Override
    public APIResponse parse(String name) {
        Parser parser = new NameParser();
        System.out.println(new SQLiteNameGenderService().getGender(name));
        var result = parser.parse(name);
        return new APIResponse(null, result);
    }

    @Override
    public List<String> getTitles() {
       return titleStorageService.getAllTitles().values().stream().toList();
    }

    @Override
    public boolean addTitle(Title title) {
        titleStorageService.addTitle(title.name(), title.regex());
        return true;
    }

    @Override
    public StructuredName save(StructuredName name) {
        //TODO: save into database
        return new StructuredName(name, StandardizedSalutationGenerator.generateStandardizedSalutation(name));
    }

    @Override
    public String status() {return "online";}
}
