package br.unisantos.pce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.unisantos.pce.model.Evacuacao;

public interface EvacuacaoRepository extends JpaRepository<Evacuacao, Integer> {
}