CREATE TABLE enderecos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cep VARCHAR(255),
    bairro VARCHAR(255),
    estado VARCHAR(255),
    cidade VARCHAR(255),
    logradouro VARCHAR(255)
);

CREATE TABLE pessoas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255),
    idade INT,
    endereco_id BIGINT,
    score INT,
    telefone VARCHAR(255),
    FOREIGN KEY (endereco_id) REFERENCES enderecos(id)
);