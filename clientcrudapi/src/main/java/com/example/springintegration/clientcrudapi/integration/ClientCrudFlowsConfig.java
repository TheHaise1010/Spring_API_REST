package com.example.springintegration.clientcrudapi.integration;

import com.example.springintegration.clientcrudapi.model.*;
import com.example.springintegration.clientcrudapi.service.ClienteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder; // Para crear mensajes

@Configuration
public class ClientCrudFlowsConfig {

    private final ClienteService clienteService;

    public ClientCrudFlowsConfig(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // --- 1. Flujo para CREAR Cliente (POST /api/clientes) ---
    @Bean
    public IntegrationFlow createClientFlow() {
        return IntegrationFlows.from(
                        Http.inboundGateway("/api/clientes")
                                .requestMapping(m -> m.methods(HttpMethod.POST)
                                        .consumes("application/json")
                                        .produces("application/json"))
                                .statusCodeExpression("200")) // Estado HTTP 200 OK por defecto
                .transform(new JsonToObjectTransformer(ClienteDto.class)) // JSON a ClienteDto
                .handle(clienteService, "createClient") // Llama al servicio para crear
                .transform(new ObjectToJsonTransformer()) // Resultado a JSON
                .get();
    }

    // --- 2. Flujo para OBTENER Cliente por DUI (GET /api/clientes/{dui}) ---
    @Bean
    public IntegrationFlow getClientByDuiFlow() {
        return IntegrationFlows.from(
                        Http.inboundGateway("/api/clientes/{dui}") // Path variable {dui}
                                .requestMapping(m -> m.methods(HttpMethod.GET)
                                        .produces("application/json"))
                                .statusCodeExpression("200"))
                // Extrae la variable 'dui' del path y la usa como payload para el servicio
                .handle((Message<?> m) -> {
                    String dui = (String) m.getHeaders().get("dui"); // "dui" es el nombre de la path variable
                    return clienteService.getClientByDui(dui); // Llama al servicio con el DUI
                })
                .transform(new ObjectToJsonTransformer()) // Resultado a JSON
                .get();
    }

    // --- 3. Flujo para OBTENER TODOS los Clientes (GET /api/clientes) ---
    @Bean
    public IntegrationFlow getAllClientsFlow() {
        return IntegrationFlows.from(
                        Http.inboundGateway("/api/clientes") // Endpoint sin path variable
                                .requestMapping(m -> m.methods(HttpMethod.GET)
                                        .produces("application/json"))
                                .statusCodeExpression("200"))
                .handle(clienteService, "getAllClients") // Llama al servicio para obtener todos
                .transform(new ObjectToJsonTransformer()) // Resultado a JSON
                .get();
    }

    // --- 4. Flujo para ACTUALIZAR Cliente (PUT /api/clientes/{dui}) ---
    @Bean
    public IntegrationFlow updateClientFlow() {
        return IntegrationFlows.from(
                        Http.inboundGateway("/api/clientes/{dui}") // Path variable {dui}
                                .requestMapping(m -> m.methods(HttpMethod.PUT)
                                        .consumes("application/json")
                                        .produces("application/json"))
                                .statusCodeExpression("200"))
                .transform(new JsonToObjectTransformer(ClienteDto.class)) // JSON a ClienteDto
                // Pasa tanto el DUI (de la URL) como el ClienteDto (del cuerpo) al servicio
                .handle((Message<ClienteDto> m) -> {
                    String duiFromPath = (String) m.getHeaders().get("dui"); // Extrae DUI de la URL
                    ClienteDto requestBody = m.getPayload(); // Obtiene el cuerpo de la petición
                    return clienteService.updateClient(duiFromPath, requestBody); // Llama al servicio
                })
                .transform(new ObjectToJsonTransformer()) // Resultado a JSON
                .get();
    }

    // --- 5. Flujo para ELIMINAR Cliente (DELETE /api/clientes/{dui}) ---
    @Bean
    public IntegrationFlow deleteClientFlow() {
        return IntegrationFlows.from(
                        Http.inboundGateway("/api/clientes/{dui}") // Path variable {dui}
                                .requestMapping(m -> m.methods(HttpMethod.DELETE)
                                        .produces("application/json"))
                                .statusCodeExpression("200"))
                .handle((Message<?> m) -> {
                    String dui = (String) m.getHeaders().get("dui"); // Extrae DUI de la URL
                    return clienteService.deleteClient(dui); // Llama al servicio para eliminar
                })
                .transform(new ObjectToJsonTransformer()) // Resultado a JSON
                .get();
    }
}