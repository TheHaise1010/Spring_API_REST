package com.example.springintegration.clientcrudapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDto {
    private Long id;
    private String dui;
    private String primerNombre;
    private String apellido;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    // Constructor para convertir de entidad Cliente a DTO
    public ClienteResponseDto(Cliente cliente) {
        this.id = cliente.getId();
        this.dui = cliente.getDui();
        this.primerNombre = cliente.getPrimerNombre();
        this.apellido = cliente.getApellido();
        this.fechaNacimiento = cliente.getFechaNacimiento();
    }
}