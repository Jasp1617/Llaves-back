package com.example.sena.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sena.Model.Entity.Usuarios;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuarios, UUID>{
    
    Optional<Usuarios> findFirstByEmail(String email);
}
