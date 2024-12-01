-- Criação da tabela "atendimento"
CREATE TABLE IF NOT EXISTS atendimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_atendimento VARCHAR(255),
    descricao VARCHAR(255)
    );

-- Criação da tabela "paciente"
CREATE TABLE IF NOT EXISTS paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_paciente VARCHAR(255) NOT NULL,
    situacao VARCHAR(255) NOT NULL,
    sala VARCHAR(255),
    posicao_na_fila INT
    );

-- Criação da tabela "medico"
CREATE TABLE IF NOT EXISTS medico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    crm VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    disponivel BOOLEAN DEFAULT TRUE -- Coluna para indicar se o médico está disponível
    );

-- Inserção de dados na tabela "atendimento"
INSERT INTO atendimento (tipo_atendimento, descricao) VALUES
    ('consulta', 'Atendimento médico geral'),
    ('emergencia', 'Atendimento para situações de emergência'),
    ('coleta', 'Coleta de exames laboratoriais');


