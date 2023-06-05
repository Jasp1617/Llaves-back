package com.example.sena.Controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sena.Configuration.exception.EmailNotAvaliableException;
import com.example.sena.Configuration.exception.EntityAlreadyOnStateException;
import com.example.sena.Configuration.security.model.dto.NewUserRequest;
import com.example.sena.Model.DTO.UsuariosDTO;
import com.example.sena.Model.Entity.Usuarios;
import com.example.sena.Model.Mapper.UsuarioMapper;
import com.example.sena.Model.enums.AuthorityEnum;
import com.example.sena.Service.Service.UsuariosService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;
    private final UsuarioMapper usuarioMapper;

    @GetMapping("/paginate")
    public ResponseEntity<Page<UsuariosDTO>> handleGetUsuarios(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "9") final Integer size) {

        final Pageable paging = PageRequest.of(page, size);
        final Page<Usuarios> pageUsuarios = usuariosService.getUsuariosPaginate(paging);
        final Page<UsuariosDTO> pageUsuariosDTO = new PageImpl<UsuariosDTO>(
                pageUsuarios.map(usuarioMapper::pojoToDto).toList());
        return ResponseEntity.status(HttpStatus.OK).body(pageUsuariosDTO);

    }

    @PostMapping("/registrar")
    public ResponseEntity<HttpStatus> handleCreateUsuario(@RequestBody NewUserRequest newUserRequest)
            throws EntityAlreadyOnStateException {
        if (!usuariosService.emailDisponible(newUserRequest.getEmail())) {
            throw new EmailNotAvaliableException(String.format("Email no disponible"));
        }
        usuariosService.createNewUser(newUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update/{idUsuario}")
    public ResponseEntity<HttpStatus> handleUsuarioInformation(@PathVariable UUID idUsuario,
            @RequestBody final UsuariosDTO usuarioDTO)
            throws EntityNotFoundException, IllegalAccessException {
        Optional<Usuarios> optional = usuariosService.getUsuarioById(idUsuario);
        if (!optional.isPresent()) {
            throw new EntityNotFoundException(String.format("Usuario no encontrado. UUID: %s", idUsuario));
        }
        Usuarios current = optional.get();
        Usuarios updated = usuarioMapper.dtoToPojo(usuarioDTO);
        if (!current.getAuthority().equals(AuthorityEnum.ROLE_ADMINISTRADOR)) {
            if (current.getEmail() != updated.getEmail() && usuariosService.emailDisponible(updated.getEmail())) {
                throw new EmailNotAvaliableException(String.format("Email no disponible"));
            }
            updated.setPassword(current.getPassword());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
}
