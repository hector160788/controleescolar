package com.mx.controlescolar.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion central de seguridad web para la aplicacion.
 *
 * Define rutas publicas, pagina de login, cierre de sesion, encoder de
 * contrasenas y el proveedor de autenticacion basado en {@link UserDetailsService}.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Define reglas de autorizacion y flujo web de autenticacion.
        http
                .authorizeHttpRequests(auth -> auth
            // Estas rutas se pueden abrir sin iniciar sesion.
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()
            // Cualquier otra peticion exige usuario autenticado.
                        .anyRequest().authenticated())
                .formLogin(form -> form
            // Se usa una pagina de login propia (template /login).
                        .loginPage("/login")
            // Si autentica correctamente, redirige siempre a /home.
                        .defaultSuccessUrl("/home", true)
                        .permitAll())
                .logout(logout -> logout
            // Al cerrar sesion, vuelve al login con bandera de logout.
                        .logoutSuccessUrl("/login?logout"));

    // Construye y registra la cadena de filtros de seguridad.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
    // BCrypt es el algoritmo para hash de password.
    // Spring comparara: password capturado en login vs hash almacenado en BD.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

    // DaoAuthenticationProvider delega la carga de usuario al UserDetailsService.
    // En este proyecto, esa carga viene de CustomUserDetailsService -> UsuarioRepository -> BD.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

    // Configura el encoder que valida la password ingresada contra el hash de BD.
        provider.setPasswordEncoder(passwordEncoder);

    // Este provider es el componente que Spring usa durante el proceso de login.
        return provider;
    }

}
