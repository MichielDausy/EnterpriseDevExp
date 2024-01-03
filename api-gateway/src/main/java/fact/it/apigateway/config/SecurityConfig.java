package fact.it.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.authorizeExchange(exchange ->
                exchange.pathMatchers(HttpMethod.GET, "/speedruns")
                        .permitAll()
                        .pathMatchers(HttpMethod.GET, "/speedruns/profile")
                        .permitAll()
                        .pathMatchers(HttpMethod.POST, "/speedruns/start")
                        .authenticated()
                        .pathMatchers(HttpMethod.POST, "/speedruns/end")
                        .authenticated()
                        .pathMatchers(HttpMethod.GET, "/games")
                        .permitAll()
                        .pathMatchers(HttpMethod.GET, "/games/top5")
                        .permitAll()
                        .pathMatchers(HttpMethod.GET, "/profiles")
                        .authenticated()
                        .anyExchange()
                        .authenticated()
        ).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return serverHttpSecurity.build();
    }
}
