package com.example.sena.Model.Mapper;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.sena.Model.Entity.Usuarios;
import com.example.sena.Model.enums.AuthorityEnum;



@Service
public class UserDetailsMapper implements GenericMapper<Usuarios, UserDetails> {

    @Override
    public Usuarios dtoToPojo(UserDetails dto) {
        
        AuthorityEnum authority = AuthorityEnum.valueOf(dto.getAuthorities().stream().findFirst().get().toString());

        return Usuarios.builder()
                .email(dto.getUsername())
                .password(dto.getPassword())
                .authority(authority)
                .build();
    }

    @Override
    public UserDetails pojoToDto(Usuarios pojo) {
        return User.builder()
                .username(pojo.getEmail())
                .password(pojo.getPassword())
                .authorities(pojo.getAuthority().toString())
                .build();
    }

}
