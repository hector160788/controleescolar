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

@Configuration
@EnableWebSecurity
/**
 * Configuracion principal de Spring Security.
 *
 * Esta clase define 3 piezas clave:
 * 1) Que rutas son publicas y cuales requieren autenticacion.
 * 2) Como se procesa el login/logout.
 * 3) Que mecanismo se usa para validar credenciales (usuario/password).
 *
 * Importante para identificar validacion contra base de datos:
 * - En el bean authenticationProvider(...) se inyecta UserDetailsService.
 * - Ese UserDetailsService esta implementado por CustomUserDetailsService (@Service).
 * - CustomUserDetailsService consulta la tabla de usuarios mediante UsuarioRepository.
 * - Por eso el usuario y password se validan con datos persistidos en BD.
 */
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
