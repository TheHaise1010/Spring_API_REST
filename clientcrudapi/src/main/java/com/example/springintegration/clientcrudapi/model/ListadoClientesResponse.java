package com.example.springintegration.clientcrudapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListadoClientesResponse {
    private List<ClienteResponseDto> clientes;
    private int totalClientes;
}