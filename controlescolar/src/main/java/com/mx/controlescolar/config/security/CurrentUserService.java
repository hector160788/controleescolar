package com.mx.controlescolar.config.security;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mx.controlescolar.model.entity.Usuario;
import com.mx.controlescolar.model.repository.UsuarioRepository;

@Service
public class CurrentUserService {

    private final UsuarioRepository usuarioRepository;

    public CurrentUserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String usernameOrEmpty() {
        return authenticated().map(Authentication::getName).orElse("");
    }

    public Set<String> authorities() {
        return authenticated()
                .map(Authentication::getAuthorities)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toUnmodifiableSet());
    }

    public Usuario usuarioOrNull() {
        String username = usernameOrEmpty();
        if (username.isEmpty()) {
            return null;
        }
        return usuarioRepository.findByUsuario(username).orElse(null);
    }

    private java.util.Optional<Authentication> authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(authentication);
    }
}
