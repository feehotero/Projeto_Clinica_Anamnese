package br.unisantos.pce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.unisantos.pce.model.Escolaridade;

public interface EscolaridadeRepository extends JpaRepository<Escolaridade, Integer> {
}