package com.example.namesplitter.api;

import com.example.namesplitter.model.APIResponse;
import com.example.namesplitter.model.StructuredName;
import com.example.namesplitter.model.TitleDTO;
import com.example.namesplitter.model.TitleData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This interface defines the API endpoints for the name parsing service.
 * It includes methods for parsing names, managing titles, saving structured names, and checking the service status.
 */
@RequestMapping("api")
public interface APIService {

    /**
     * This method parses a given name into its components.
     *
     * @param name The name to parse.
     * @return An APIResponse containing the parsed name and any errors.
     */
    @PostMapping("/parse")
    public APIResponse parse(@RequestBody String name);

    /**
     * This method retrieves all academic titles.
     *
     * @return A list of all academic titles.
     */
    @GetMapping("/getTitles")
    public List<TitleData> getTitles();

    /**
     * This method adds a new title.
     *
     * @param titleDTO The title to add.
     * @return A boolean indicating whether the title was successfully added.
     */
    @PostMapping("/addTitle")
    public boolean addTitle(@RequestBody TitleDTO titleDTO);

    /**
     * This method removes a title.
     *
     * @param title The title to remove.
     * @return A boolean indicating whether the title was successfully removed.
     */
    @PostMapping("/removeTitle")
    public boolean removeTitle(@RequestBody TitleData title);

    /**
     * This method saves a structured name.
     *
     * @param name The name to save.
     * @return The saved name.
     */
    @PostMapping("/save")
    public StructuredName save(@RequestBody StructuredName name);

    /**
     * This method returns the status of the service.
     *
     * @return A string indicating the status of the service.
     */
    @GetMapping("/status")
    public String status();
}