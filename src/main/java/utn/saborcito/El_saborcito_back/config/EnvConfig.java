package utn.saborcito.El_saborcito_back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "file:./.env", ignoreResourceNotFound = true)
public class EnvConfig {
    // Esta clase permite que Spring Boot cargue el archivo .env
    // Se usa "file:./.env" para buscarlo en la raíz del proyecto
    // ignoreResourceNotFound = true permite que la aplicación inicie aunque no
    // encuentre el archivo
}