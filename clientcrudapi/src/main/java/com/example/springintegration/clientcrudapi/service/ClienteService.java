package com.example.springintegration.clientcrudapi.service;

import com.example.springintegration.clientcrudapi.model.*;
import com.example.springintegration.clientcrudapi.repository.ClienteRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Para asegurar la transacción de DB

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Crea un nuevo cliente en la base de datos.
     * @param request Datos del cliente a crear.
     * @return ClienteOperationResponse con el resultado de la operación.
     */
    @Transactional
    public ClienteOperationResponse createClient(ClienteDto request) {
        System.out.println("ClienteService: Intentando crear cliente con DUI: " + request.getDui());

        // Validaciones básicas
        if (request.getDui() == null || request.getDui().isEmpty() || request.getDui().length() != 10) {
            return new ClienteOperationResponse(null, new OperationResult("FAILED", "DUI inválido. Debe tener 10 caracteres."));
        }
        if (request.getPrimerNombre() == null || request.getPrimerNombre().isEmpty()) {
            return new ClienteOperationResponse(null, new OperationResult("FAILED", "Primer nombre no puede ser nulo o vacío."));
        }
        // Puedes añadir más validaciones aquí

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setDui(request.getDui());
        nuevoCliente.setPrimerNombre(request.getPrimerNombre());
        nuevoCliente.setApellido(request.getApellido());
        nuevoCliente.setFechaNacimiento(request.getFechaNacimiento());

        try {
            Cliente clienteGuardado = clienteRepository.save(nuevoCliente);
            System.out.println("ClienteService: Cliente " + clienteGuardado.getDui() + " creado exitosamente.");
            return new ClienteOperationResponse(new ClienteResponseDto(clienteGuardado), new OperationResult("SUCCESS", "Cliente creado exitosamente."));
        } catch (DataIntegrityViolationException e) {
            System.err.println("ClienteService: Error al crear cliente - DUI duplicado: " + request.getDui());
            return new ClienteOperationResponse(null, new OperationResult("FAILED", "El DUI '" + request.getDui() + "' ya existe."));
        } catch (Exception e) {
            System.err.println("ClienteService: Error inesperado al crear cliente: " + e.getMessage());
            return new ClienteOperationResponse(null, new OperationResult("ERROR", "Ocurrió un error inesperado al crear al cliente."));
        }
    }

    /**
     * Obtiene un cliente por su DUI.
     * @param dui El DUI del cliente a buscar.
     * @return ClienteResponseDto si se encuentra, o un mensaje de error si no.
     */
    @Transactional(readOnly = true) // Solo lectura, optimiza el rendimiento
    public Object getClientByDui(String dui) { // Retorna Object porque puede ser ClienteResponseDto o OperationResult
        System.out.println("ClienteService: Buscando cliente con DUI: " + dui);
        return clienteRepository.findByDui(dui)
                .map(ClienteResponseDto::new) // Si se encuentra, lo mapea a DTO
                .orElseGet(() -> new OperationResult("NOT_FOUND", "Cliente con DUI '" + dui + "' no encontrado.")); // Si no, devuelve un OperationResult
    }

    /**
     * Obtiene todos los clientes registrados.
     * @return ListadoClientesResponse con la lista de clientes.
     */
    @Transactional(readOnly = true)
    public ListadoClientesResponse getAllClients() {
        System.out.println("ClienteService: Obteniendo todos los clientes.");
        List<ClienteResponseDto> clientes = clienteRepository.findAll().stream()
                .map(ClienteResponseDto::new)
                .collect(Collectors.toList());
        return new ListadoClientesResponse(clientes, clientes.size());
    }

    /**
     * Actualiza un cliente existente por su DUI.
     * @param dui El DUI del cliente a actualizar (del path).
     * @param request Los nuevos datos del cliente (del cuerpo de la petición).
     * @return ClienteOperationResponse con el resultado de la operación.
     */
    @Transactional
    public ClienteOperationResponse updateClient(String dui, ClienteDto request) {
        System.out.println("ClienteService: Intentando actualizar cliente con DUI: " + dui);

        return clienteRepository.findByDui(dui)
                .map(existingClient -> {
                    // Actualizar campos
                    existingClient.setPrimerNombre(request.getPrimerNombre());
                    existingClient.setApellido(request.getApellido());
                    existingClient.setFechaNacimiento(request.getFechaNacimiento());
                    // NOTA: El DUI no se actualiza ya que es UNIQUE y la clave de búsqueda.
                    // Si se permitiera actualizar el DUI, necesitarías lógica adicional.

                    try {
                        Cliente updatedClient = clienteRepository.save(existingClient); // Save actualiza si ya tiene ID
                        System.out.println("ClienteService: Cliente con DUI " + dui + " actualizado exitosamente.");
                        return new ClienteOperationResponse(new ClienteResponseDto(updatedClient), new OperationResult("SUCCESS", "Cliente actualizado exitosamente."));
                    } catch (Exception e) {
                        System.err.println("ClienteService: Error al actualizar cliente con DUI " + dui + ": " + e.getMessage());
                        return new ClienteOperationResponse(null, new OperationResult("ERROR", "Ocurrió un error al actualizar al cliente."));
                    }
                })
                .orElseGet(() -> {
                    System.err.println("ClienteService: No se encontró cliente con DUI " + dui + " para actualizar.");
                    return new ClienteOperationResponse(null, new OperationResult("NOT_FOUND", "Cliente con DUI '" + dui + "' no encontrado para actualizar."));
                });
    }

    /**
     * Elimina un cliente por su DUI.
     * @param dui El DUI del cliente a eliminar.
     * @return ClienteOperationResponse con el resultado de la operación.
     */
    @Transactional
    public ClienteOperationResponse deleteClient(String dui) {
        System.out.println("ClienteService: Intentando eliminar cliente con DUI: " + dui);

        return clienteRepository.findByDui(dui)
                .map(clientToDelete -> {
                    try {
                        clienteRepository.delete(clientToDelete);
                        System.out.println("ClienteService: Cliente con DUI " + dui + " eliminado exitosamente.");
                        return new ClienteOperationResponse(new ClienteResponseDto(clientToDelete), new OperationResult("SUCCESS", "Cliente eliminado exitosamente."));
                    } catch (Exception e) {
                        System.err.println("ClienteService: Error al eliminar cliente con DUI " + dui + ": " + e.getMessage());
                        return new ClienteOperationResponse(null, new OperationResult("ERROR", "Ocurrió un error al eliminar al cliente."));
                    }
                })
                .orElseGet(() -> {
                    System.err.println("ClienteService: No se encontró cliente con DUI " + dui + " para eliminar.");
                    return new ClienteOperationResponse(null, new OperationResult("NOT_FOUND", "Cliente con DUI '" + dui + "' no encontrado para eliminar."));
                });
    }
}