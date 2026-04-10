package com.posstudio.papel.security.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

import com.posstudio.papel.security.Exception.CredencialesInvalidasException;
import com.posstudio.papel.security.dto.request.LoginRequestDTO;
import com.posstudio.papel.security.dto.response.LoginResponseDTO;
import com.posstudio.papel.security.model.Usuario;
import com.posstudio.papel.security.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request,
            HttpServletResponse response) {

        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(

                            request.usuario(),
                            request.contrasena()));
            Usuario usuarioAuth = (Usuario) auth.getPrincipal();

            String jwt = jwtUtils.generateToken(usuarioAuth);

            // 🍪 Cookie HttpOnly
            ResponseCookie cookie = ResponseCookie.from("token", jwt)
                    .httpOnly(true)
                    .secure(false) // ⚠️ true en producción
                    .path("/") // 🔥 CLAVE
                    .maxAge(60 * 60 * 24) // 1 día
                    .sameSite("Strict")
                    .build();
            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return new LoginResponseDTO(usuarioAuth.getId(), usuarioAuth.getNombre(), usuarioAuth.getRol().name(),
                    cookie.getValue());
        } catch (Exception e) {
            throw new CredencialesInvalidasException(e.toString());
        }
    }
}