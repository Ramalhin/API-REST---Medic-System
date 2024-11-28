package com.clinica.demo.repository;

import com.clinica.demo.model.Paciente;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findAllByOrderByPosicaoNaFilaAsc();
    List<Paciente> findByTipoAtendimento(String tipoAtendimento, Sort sort);
    Optional<Paciente> findFirstByTipoAtendimento(String tipoAtendimento);
    List<Paciente> findAllByTipoAtendimento(String tipoAtendimento, Sort sort);
    List<Paciente> findAllByTipoAtendimentoNot(String tipoAtendimento, Sort sort);
    List<Paciente> findBySituacao(String situacao);
    Optional<Paciente> findFirstBySituacaoOrderByPosicaoNaFila(String situacao);
    List<Paciente> findAllBySituacaoNot(String situacao, Sort sort);
    @Query("SELECT p.numeroPaciente FROM Paciente p WHERE p.tipoAtendimento = :tipo AND p.situacao = 'Atendido' ORDER BY p.id DESC LIMIT 1")
    String findUltimoPacientePorTipo(@Param("tipo") String tipo);

    @Query("SELECT p.numeroPaciente FROM Paciente p WHERE p.situacao = 'Em Atendimento' ORDER BY p.posicaoNaFila ASC LIMIT 1")
    String findProximoPaciente();

    Paciente findTopByOrderByIdDesc();

    Paciente findTopBySituacaoOrderByIdDesc(String situacao);
}
