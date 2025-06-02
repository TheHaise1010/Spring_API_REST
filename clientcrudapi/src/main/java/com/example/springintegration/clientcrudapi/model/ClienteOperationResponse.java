package com.example.springintegration.clientcrudapi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteOperationResponse {
    private ClienteResponseDto cliente; // Puede ser null en caso de error o eliminación
    private OperationResult result;
}