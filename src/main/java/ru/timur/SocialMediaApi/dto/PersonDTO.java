package ru.timur.SocialMediaApi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class PersonDTO {
    @JsonProperty(namespace = "id")
    private int id;
    @NotEmpty(message = "This field should not be empty")
    @Size(min = 3,max = 100,message = "This field must be between 3 and 100 characters")
    @JsonProperty(namespace = "username")
    private String username;
    @Email(message = "Invalid email address")
    @NotEmpty(message = "This field should not be empty")
    @UniqueEmail
    @JsonProperty(namespace = "email")
    private String email;
    @NotEmpty(message = "This field should not be empty")
    @JsonProperty(namespace = "password")
    private String password;
    @JsonProperty(namespace = "role")
    private String role;
}
