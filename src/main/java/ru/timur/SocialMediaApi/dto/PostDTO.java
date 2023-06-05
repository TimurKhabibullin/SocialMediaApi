package ru.timur.SocialMediaApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDTO {
    @JsonProperty(namespace = "id")
    private int id;
    @Size(min = 10, max = 200, message = "This field must be between 10 and 200 characters")
    @NotEmpty(message = "This field should not be empty")
    @JsonProperty(namespace = "header")
    private String header;
    @NotEmpty(message = "This field should not be empty")
    @Size(min = 10, max = 200, message = "This field must be between 100 and 2000 characters")
    @JsonProperty(namespace = "text")
    private String text;
    @NotEmpty(message = "This field should not be empty")
    @JsonProperty(namespace = "linkForImage")
    private String linkForImage;
    @JsonProperty(namespace = "dateOfCreate")
    private long dateOfCreate;
    @JsonProperty(namespace = "person")
    private int person;
}
