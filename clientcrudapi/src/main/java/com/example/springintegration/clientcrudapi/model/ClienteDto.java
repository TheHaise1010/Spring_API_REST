package com.example.springintegration.clientcrudapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
    private String dui;
    private String primerNombre;
    private String apellido;
    @JsonFormat(pattern = "yyyy-MM-dd") // Esperamos la fecha en formato "YYYY-MM-DD"
    private LocalDate fechaNacimiento;
}