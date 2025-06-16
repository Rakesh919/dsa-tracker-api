package com.company.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProblemDTO {

    @NotNull(message = "Title can't be empty")
    private String title;

    @NotNull(message = "Description can't be empty")
    private String description;

    @NotNull(message = "Language can't be empty")
    private String language;

    @NotNull(message = "Difficulty level can't be empty")
    private String difficulty;

    @NotEmpty(message = "topics must contain one value")
    @Size(max=5, min=1, message = "Topics must contain 1 to 5 items")
    private List<String> topics;

    @NotEmpty(message = "Add at least one link")
    @Size(min=1,max=3,message = "Links must contain 1 to 3 items")
    private List<String> links;

//    @NotEmpty(message ="Must contain at least one solution " )
//    private List<String> solutions;
}
