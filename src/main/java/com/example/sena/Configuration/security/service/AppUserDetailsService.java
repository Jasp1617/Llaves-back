package com.example.sena.Configuration.security.service;

import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.sena.Model.Entity.Usuarios;
import com.example.sena.Model.Mapper.UserDetailsMapper;
import com.example.sena.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;


@Service
@Primary
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuariosRepository;
    private final UserDetailsMapper userDetailsMapper;

    /**
    Carga los detalles del usuario por su nombre de usuario.
    @param username El nombre de usuario.
    @return Los detalles del usuario correspondiente.
    @throws UsernameNotFoundException si el usuario no existe.
    */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuarios> usuario = usuariosRepository.findFirstByEmail(username);
        if (!usuario.isPresent()) {
            throw new UsernameNotFoundException("User details not found: " + username);
        }
        return  userDetailsMapper.pojoToDto(usuario.get());
    }

    /**
    Carga los detalles de un usuario dado su nombre de usuario.
    @param username El nombre de usuario del usuario a cargar.
    @return Los detalles del usuario especificado.
    @throws UsernameNotFoundException Si no se encuentra al usuario con el nombre de usuario proporcionado.
    */
    public Usuarios loadUsuarioByUsername(String username) throws UsernameNotFoundException { 
        Optional<Usuarios> customerOptional = usuariosRepository.findFirstByEmail(username);
        if (!customerOptional.isPresent()) {
            throw new UsernameNotFoundException("User details not found: " + username);
        }
        return  customerOptional.get();
    }
}
