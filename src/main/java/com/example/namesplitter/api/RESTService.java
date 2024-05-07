package com.example.namesplitter.api;

import com.example.namesplitter.model.*;
import com.example.namesplitter.parser.NameParser;
import com.example.namesplitter.parser.Parser;
import com.example.namesplitter.helper.StandardizedSalutationGenerator;
import com.example.namesplitter.storage.InMemoryTitleStorage;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RESTService implements APIService{

    public TitleStorageService titleStorageService = InMemoryTitleStorage.getInstance();

    @Override
    public APIResponse parse(String name) {
        Parser parser = new NameParser();
        var result = parser.parse(name);
        return new APIResponse(result.getRight().stream().map(excpt -> new ErrorDTO(excpt.getMessage(), excpt.getStartPos(), excpt.getEndPos())).toList(), result.getLeft());
    }

    @Override
    public List<String> getTitles() {
        return titleStorageService.getAllAcademicTitles().stream().map(TitleData::name).toList();
    }

    @Override
    public boolean addTitle(TitleDTO titleDTO) {
        return titleStorageService.addTitle(titleDTO.name(), titleDTO.regex());
    }

    @Override
    public boolean removeTitle(String title) {
        return titleStorageService.removeTitle(title);
    }


    @Override
    public StructuredName save(StructuredName name) {
        //TODO: save into database
        return new StructuredName(name, StandardizedSalutationGenerator.generateStandardizedSalutation(name));
    }

    @Override
    public String status() {return "online";}
}
