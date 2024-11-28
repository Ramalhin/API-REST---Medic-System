package com.clinica.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoAtendimento;
    private String numeroPaciente;
    private String situacao;
    private String sala;
    private Long posicaoNaFila;
    private String medico; // Novo campo para o médico responsável

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPaciente() {
        return numeroPaciente;
    }

    public void setNumeroPaciente(String numeroPaciente) {
        this.numeroPaciente = numeroPaciente;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public Long getPosicaoNaFila() {
        return posicaoNaFila;
    }

    public void setPosicaoNaFila(Long posicaoNaFila) {
        this.posicaoNaFila = posicaoNaFila;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }
    public String getTipoAtendimento() {
        return tipoAtendimento;
    }
    public void setTipoAtendimento(String tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }
}
