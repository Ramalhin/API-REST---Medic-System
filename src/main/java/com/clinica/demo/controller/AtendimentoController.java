package com.clinica.demo.controller;

import com.clinica.demo.model.Atendimento;
import com.clinica.demo.model.Paciente;
import com.clinica.demo.repository.AtendimentoRepository;
import com.clinica.demo.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/atendimento")
public class AtendimentoController {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    private Random random = new Random();

    // Adicionar paciente para consulta
    @PostMapping("/consulta")
    public Paciente adicionarConsulta() {
        return gerarPaciente("consulta", false);
    }

    // Adicionar paciente para emergência
    @PostMapping("/emergencia")
    public Paciente adicionarEmergencia() {
        // Encontrar a última posição de emergência atual
        long ultimaPosicaoEmergencia = pacienteRepository.findAllByTipoAtendimento("emergencia", Sort.by(Sort.Direction.ASC, "posicaoNaFila"))
                .stream()
                .mapToLong(Paciente::getPosicaoNaFila)
                .max()
                .orElse(0L);

        // Criar um novo paciente de emergência e colocá-lo na primeira posição
        Paciente pacienteEmergencia = gerarPaciente("emergencia", true);
        pacienteEmergencia.setPosicaoNaFila(ultimaPosicaoEmergencia + 1);
        pacienteRepository.save(pacienteEmergencia);

        // Atualizar as posições dos demais pacientes (consulta e coleta)
        atualizarPosicoesNaoEmergencia(pacienteEmergencia.getPosicaoNaFila());

        return pacienteEmergencia;
    }

    // Adicionar paciente para coleta
    @PostMapping("/coleta")
    public Paciente adicionarColeta() {
        return gerarPaciente("coleta", false);
    }

    // Método para atualizar as posições dos pacientes que não são emergência
    private void atualizarPosicoesNaoEmergencia(long novaPosicaoInicial) {
        List<Paciente> pacientesNaoEmergencia = pacienteRepository.findAllByTipoAtendimentoNot("emergencia", Sort.by(Sort.Direction.ASC, "posicaoNaFila"));

        long novaPosicao = novaPosicaoInicial + 1;
        for (Paciente paciente : pacientesNaoEmergencia) {
            paciente.setPosicaoNaFila(novaPosicao);
            novaPosicao++;
            pacienteRepository.save(paciente);
        }
    }

    // Método para gerar paciente
    private Paciente gerarPaciente(String tipoAtendimento, boolean prioridadeAlta) {
        // Cria um novo atendimento
        Atendimento atendimento = new Atendimento(tipoAtendimento);
        atendimento.setPrioridade(prioridadeAlta ? 1 : 0);
        atendimentoRepository.save(atendimento);

        // Gera um número de paciente único
        String numeroPaciente = "P" + String.format("%04d", atendimento.getId()); // ID do atendimento como identificador único

        // Define uma sala aleatória
        String[] salas = {"Sala 1", "Sala 2", "Sala 3", "Sala 4"};
        String sala = salas[random.nextInt(salas.length)];

        // Seleciona um médico aleatório
        String nomeMedico = "Nenhum Médico Disponível";

        // Cria um novo paciente
        Paciente paciente = new Paciente();
        paciente.setNumeroPaciente(numeroPaciente);
        paciente.setSituacao(tipoAtendimento);
        paciente.setSala(sala);
        paciente.setPosicaoNaFila(pacienteRepository.count()+1L); // Última posição na fila
        paciente.setMedico(nomeMedico);
        paciente.setTipoAtendimento(tipoAtendimento);

        // Salva o paciente no repositório
        return pacienteRepository.save(paciente);
    }
    @PostMapping("/chamarProximo")
    public Paciente chamarProximoPaciente() {
        List<Paciente> fila = pacienteRepository.findAllBySituacaoNot("Atendido", Sort.by(Sort.Direction.ASC, "posicaoNaFila"));
        if (fila.isEmpty()) {
            throw new RuntimeException("Nenhum paciente na fila.");
        }
        Paciente proximoPaciente = fila.get(0);
        proximoPaciente.setSituacao("Atendido");
        pacienteRepository.save(proximoPaciente);
        return proximoPaciente;
    }
    @GetMapping("/paciente/{id}")
    public ResponseEntity<Paciente> obterPaciente(@PathVariable Long id) {
        Optional<Paciente> pacienteOpt = pacienteRepository.findById(id);
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            return ResponseEntity.ok(paciente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Retornar a fila de atendimentos
    @GetMapping("/fila")
    public List<Paciente> obterFila() {
        return pacienteRepository.findAll(Sort.by(Sort.Direction.ASC, "posicaoNaFila"));
    }
    @GetMapping("/tipo/{tipoAtendimento}")
    public ResponseEntity<List<Atendimento>> listarAtendimentosPorTipo(@PathVariable String tipoAtendimento) {
        System.out.println("Recebendo requisição para tipo: " + tipoAtendimento);
        List<Atendimento> atendimentos = atendimentoRepository.findAllByTipoAtendimento(tipoAtendimento);
        return ResponseEntity.ok(atendimentos);
    }


}