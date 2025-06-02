package com.example.springintegration.clientcrudapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot para la API CRUD de clientes.
 * Esta clase es el punto de entrada que inicia el contexto de Spring.
 */
@SpringBootApplication // Esta anotación combina:
// @Configuration: Configura esta clase como fuente de definiciones de beans.
// @EnableAutoConfiguration: Configura Spring Boot para que intente
//                          configurar automáticamente la aplicación Spring.
// @ComponentScan: Busca componentes, configuraciones y servicios en el paquete
//                 'com.example.springintegration.clientcrudapi' y sus subpaquetes.
public class ClientCrudApiApplication {

    /**
     * El método main es el punto de entrada estándar para las aplicaciones Java.
     * En una aplicación Spring Boot, este método utiliza SpringApplication.run()
     * para arrancar la aplicación, que a su vez inicializa el contexto de Spring
     * y el servidor web integrado (Tomcat por defecto).
     *
     * @param args Argumentos de la línea de comandos pasados a la aplicación.
     */
    public static void main(String[] args) {
        // Ejecuta la aplicación Spring Boot.
        // La clase 'ClientCrudApiApplication.class' se pasa como argumento para indicar
        // el origen de las configuraciones y componentes de la aplicación.
        SpringApplication.run(ClientCrudApiApplication.class, args);
    }

}