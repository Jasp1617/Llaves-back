package com.example.sena.Model.DTO;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({ "id", "nombre", "apellido", "email", "estado" })

public class UsuariosDTO {
    private UUID id;

    private String nombre;

    private String apellido;

    @Email
    private String email;

    @JsonIgnore
    private String password;
    
    @JsonIgnore
    private String authority;

    private Boolean estado;
}
