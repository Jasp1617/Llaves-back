package com.example.sena.Model.Mapper;

import org.springframework.stereotype.Service;

import com.example.sena.Model.DTO.UsuariosDTO;
import com.example.sena.Model.Entity.Usuarios;
import com.example.sena.Model.enums.AuthorityEnum;

@Service
public class UsuarioMapper implements GenericMapper<Usuarios, UsuariosDTO> {

    @Override
    public Usuarios dtoToPojo(UsuariosDTO dto) {
        Usuarios usuario = Usuarios.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .authority(AuthorityEnum.valueOf(dto.getAuthority()))
                .estado(dto.getEstado())
                .build();
        if (dto.getId() != null) {
            usuario.setId(dto.getId());
        }
        return usuario;
    }

    @Override
    public UsuariosDTO pojoToDto(Usuarios pojo) {
        UsuariosDTO usuarioDTO = UsuariosDTO.builder()
                .id(pojo.getId())
                .nombre(pojo.getNombre())
                .apellido(pojo.getApellido())
                .email(pojo.getEmail())
                .password("")
                .authority(pojo.getAuthority().toString())
                .estado(pojo.getEstado())
                .build();
        return usuarioDTO;
    }

}
