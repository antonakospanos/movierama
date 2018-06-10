package org.antonakospanos.movierama.web.dto.movies;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;

@JsonPropertyOrder({ "title", "description" })
public class MovieBaseDto {

    @NotEmpty
    @ApiModelProperty(example = "Star Wars", required = true)
    private String title;

    @ApiModelProperty(example = "A long time ago in a galaxy far, far away..")
    private String description;

    public MovieBaseDto() {
    }

    public MovieBaseDto(String title, String description) {
        setTitle(title);
        setDescription(description);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Factory methods
    public MovieBaseDto title(String title) {
        setTitle(title);
        return this;
    }

    public MovieBaseDto description(String description) {
        setDescription(description);
        return this;
    }

}

