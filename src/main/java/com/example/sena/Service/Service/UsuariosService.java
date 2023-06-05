package com.example.sena.Service.Service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.sena.Configuration.security.model.dto.NewUserRequest;
import com.example.sena.Configuration.security.service.AuthenticationService;
import com.example.sena.Model.Entity.Usuarios;
import com.example.sena.Model.enums.AuthorityEnum;
import com.example.sena.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuariosService {
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuariosRepository;
    private final AuthenticationService authenticationService;

    public boolean emailDisponible(String email) {
        Example<Usuarios> example = Example.of(Usuarios.builder().email(email).build());
        return !usuariosRepository.exists(example);
    }

    public void createNewUser(NewUserRequest newUserRequest) {
        Usuarios newUser = Usuarios.builder()
                .nombre(newUserRequest.getNombre())
                .apellido(newUserRequest.getApellido())
                .email(newUserRequest.getEmail())
                .password(passwordEncoder.encode(newUserRequest.getPassword()))
                .authority(AuthorityEnum.ROLE_ADMINISTRADOR)
                .build();
        Usuarios saved = usuariosRepository.save(newUser);
        authenticationService.registerNewUserToken(saved);
    }

    public void registerUser(Usuarios usuarios) {
        Usuarios saved = usuariosRepository.save(usuarios);
        authenticationService.registerNewUserToken(saved);
    }

    public Optional<Usuarios> getUsuarioById(UUID idUsuario) {
        return usuariosRepository.findById(idUsuario);
    }

    public Page<Usuarios> getUsuariosPaginate(Pageable paging) {
        return usuariosRepository.findAll(paging);
    }
}
