package com.example.namesplitter.api;

import com.example.namesplitter.model.*;
import com.example.namesplitter.parser.interfaces.IParser;
import com.example.namesplitter.parser.Parser;
import com.example.namesplitter.helper.StandardizedSalutationGenerator;
import com.example.namesplitter.storage.InMemoryTitleStorage;
import com.example.namesplitter.storage.interfaces.TitleStorageService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class provides a RESTful API service for name parsing and title management.
 * It implements the APIService interface.
 */
@RestController
public class RESTService implements APIService{

    /**
     * An instance of TitleStorageService to access the titles.
     */
    public TitleStorageService titleStorageService = InMemoryTitleStorage.getInstance();

    /**
     * This method parses a given name into its components.
     * It creates a new Parser, parses the name, and returns an APIResponse.
     * The APIResponse contains a list of errors (if any) and the parsed name.
     *
     * @param name The name to parse.
     * @return An APIResponse containing the parsed name and any errors.
     */
    @Override
    public APIResponse parse(String name) {
        IParser parser = new Parser();
        var result = parser.parse(name);
        return new APIResponse(result.getRight().stream().map(excpt -> new ErrorDTO(excpt.getMessage(), excpt.getStartPos(), excpt.getEndPos())).toList(), result.getLeft());
    }

    /**
     * This method retrieves all academic titles from the title storage.
     *
     * @return A list of all academic titles.
     */
    @Override
    public List<TitleData> getTitles() {
        return titleStorageService.getAllAcademicTitles();
    }

    /**
     * This method adds a new title to the title storage.
     *
     * @param title The title to add.
     * @return A boolean indicating whether the title was successfully added.
     */
    @Override
    public boolean addTitle(TitleData title) {
        return titleStorageService.addTitle(title);
    }

    /**
     * This method removes a title from the title storage.
     *
     * @param title The title to remove.
     * @return A boolean indicating whether the title was successfully removed.
     */
    @Override
    public boolean removeTitle(TitleData title) {
        return titleStorageService.removeTitle(title);
    }

    /**
     * This method saves a structured name and generates a standardized salutation for it.
     *
     * @param name The name to save.
     * @return The saved name with a standardized salutation.
     */
    @Override
    public StructuredName save(StructuredName name) {
        return new StructuredName(name, StandardizedSalutationGenerator.generateStandardizedSalutation(name));
    }

    /**
     * This method returns the status of the service.
     *
     * @return A string indicating the status of the service.
     */
    @Override
    public String status() {return "online";}
}