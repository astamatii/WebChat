package com.astamatii.endava.webchat.dto;

import com.astamatii.endava.webchat.models.Theme;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonDto {

    @NotNull
    @Size(min = 3, max = 30, message = "The name must be between 3 and 30 letters in length")
    private String name;

    @NotNull
    @Size(min = 3, max = 15, message = "The username must be between 3 and 15 letters in length")
    private String username;

    @NotNull
    @Email(message = "The email should look like this: your_email@email.com")
    private String email;

    @NotNull
    private String password;


    //@DateTimeFormat(pattern = "dd.MM.yyyy") //throws DateTimeParseException ("Date format should be dd.MM.yyyy")
    private LocalDate dob;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Hex value must be in the format #RRGGBB")
    private String textColor;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Theme theme;

    private String language;

    private String country;

    private String city;
}
