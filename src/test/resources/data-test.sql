-- Limpeza das tabelas
DELETE FROM atendimento;
DELETE FROM paciente;
DELETE FROM medico;

-- Inserção de dados na tabela "atendimento"
INSERT INTO atendimento (tipo_atendimento, descricao)
VALUES
    ('consulta', 'Atendimento médico geral'),
    ('emergencia', 'Atendimento para situações de emergência'),
    ('coleta', 'Coleta de exames laboratoriais');

-- Inserção de dados na tabela "medico"
INSERT INTO medico (nome, crm, email, senha)
VALUES
    ('Dr. João', 'CRM001', 'joao@clinica.com', '12345');
