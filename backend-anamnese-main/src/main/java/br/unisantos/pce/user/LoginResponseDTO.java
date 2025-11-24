package br.unisantos.pce.user;

public record LoginResponseDTO(Integer usuarioId, String usuarioNome, String token) {
}
