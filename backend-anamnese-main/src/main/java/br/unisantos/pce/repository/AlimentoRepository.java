package br.unisantos.pce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.unisantos.pce.model.Alimento;

public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {
}
