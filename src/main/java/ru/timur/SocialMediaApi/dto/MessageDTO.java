package ru.timur.SocialMediaApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MessageDTO {
    @NotEmpty(message = "This field should not be empty")
    @JsonProperty("sender")
    private String sender;
    @NotEmpty(message = "This field should not be empty")
    @JsonProperty("recipient")
    private String recipient;
    @NotEmpty(message = "This field should not be empty")
    @JsonProperty("text")
    private String text;
}
