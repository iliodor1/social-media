package ru.eldar.socialmedia.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Open api configuration class
 *
 * @author eldar
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Social Media Api",
                description = "Simple Social Media", version = "0.0.1",
                contact = @Contact(
                        name = "Eldar",
                        email = "iliodor19@gmail.com"
                )
        )
)
public class OpenApiConfig {

}
