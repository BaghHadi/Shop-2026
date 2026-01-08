package fr.esiea.shop2026.adapters.infrastructure.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI shopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shop 2026 API")
                        .description("API de gestion E-commerce (Produits, Paniers, Commandes) pour le projet ESIEA.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Abdelhadi , Julian")
                                .email("abaghdadli@et.esiea.fr")));
    }
}