package com.clinica.demo.repository;

import com.clinica.demo.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    Atendimento findFirstByOrderByPrioridadeDesc();
    // Busca todos os atendimentos pelo tipo
    List<Atendimento> findAllByTipoAtendimento(String tipoAtendimento);
}
