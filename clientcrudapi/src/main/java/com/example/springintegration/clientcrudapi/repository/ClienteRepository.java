package com.example.springintegration.clientcrudapi.repository;

import com.example.springintegration.clientcrudapi.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDui(String dui); // Método para buscar un cliente por su DUI
}