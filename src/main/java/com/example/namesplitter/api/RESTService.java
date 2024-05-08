package com.example.namesplitter.api;

import com.example.namesplitter.model.*;
import com.example.namesplitter.parser.IParser;
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
        IParser parser = new Parser();
        var result = parser.parse(name);
        return new APIResponse(result.getRight().stream().map(excpt -> new ErrorDTO(excpt.getMessage(), excpt.getStartPos(), excpt.getEndPos())).toList(), result.getLeft());
    }

    @Override
    public List<TitleData> getTitles() {
        return titleStorageService.getAllAcademicTitles();
    }

    @Override
    public boolean addTitle(TitleDTO titleDTO) {
        return titleStorageService.addTitle(titleDTO.name(), titleDTO.regex());
    }

    @Override
    public boolean removeTitle(TitleData title) {
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
