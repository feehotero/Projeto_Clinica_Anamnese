package br.unisantos.pce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.unisantos.pce.model.Profissao;

public interface ProfissaoRepository extends JpaRepository<Profissao, Integer> {
}