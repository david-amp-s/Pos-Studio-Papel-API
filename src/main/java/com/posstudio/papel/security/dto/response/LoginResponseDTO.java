package com.posstudio.papel.security.dto.response;

public record LoginResponseDTO(
                Long id,
                String nombre,
                String rol,
                String jwt

) {

}
