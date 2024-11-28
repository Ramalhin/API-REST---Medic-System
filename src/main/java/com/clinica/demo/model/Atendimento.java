package com.clinica.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Atendimento {

    @Column(name = "posicao")
    private Long posicao;

    public Long getPosicao() {
        return posicao;
    }

    public void setPosicao(Long posicao) {
        this.posicao = posicao;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_atendimento") // Nome da coluna no banco
    private String tipoAtendimento;

    private Integer prioridade;

    public Atendimento() {}

    public Atendimento(String tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
        this.prioridade = 0; // Padrão para consulta e coleta
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(String tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public Integer getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Integer prioridade) {
        this.prioridade = prioridade;
    }
}
