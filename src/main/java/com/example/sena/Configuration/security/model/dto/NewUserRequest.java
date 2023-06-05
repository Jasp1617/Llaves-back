package com.example.sena.Configuration.security.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonPropertyOrder({"nombre", "apellido","email", "password", "telefono"})
public class NewUserRequest {

    @NotBlank
    @JsonProperty(value = "nombre", required = true)
    private String nombre;

    @NotBlank
    @JsonProperty(value = "apellido", required = true)
    private String apellido;

    @NotBlank
    @JsonProperty(value = "telefono", required = true)
    private String telefono;

    @Email
    @NotBlank
    @JsonProperty(value = "email", required = true)
    private String email;

    @NotBlank
    @JsonProperty(value = "password", required = true)
    private String password;
    
}
