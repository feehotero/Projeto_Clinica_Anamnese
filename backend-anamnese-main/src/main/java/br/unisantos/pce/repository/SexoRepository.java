package br.unisantos.pce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unisantos.pce.model.Sexo;

public interface SexoRepository extends JpaRepository<Sexo, Integer> {
}