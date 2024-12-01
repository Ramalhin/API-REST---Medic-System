/*------------------------------ HOME ------------------------------*/
function redirecionar(tipoAtendimento) {
    window.location.href = `fila.html?tipo=${tipoAtendimento}`;
}

function obterParametroUrl(nome) {
    const params = new URLSearchParams(window.location.search);
    return params.get(nome);
}

function configurarPaginaInicial() {
    document.getElementById("btnConsulta").addEventListener("click", () => redirecionar("consulta"));
    document.getElementById("btnEmergencia").addEventListener("click", () => redirecionar("emergencia"));
    document.getElementById("btnColeta").addEventListener("click", () => redirecionar("coleta"));
}

/*------------------------------ FILA ------------------------------*/

function configurarPaginaFila() {
    const tipoAtendimento = obterParametroUrl("tipo") || "Desconhecido";
    document.getElementById("tipoAtendimento").textContent = tipoAtendimento;
    carregarDetalhesPaciente(tipoAtendimento);
}

function carregarDetalhesPaciente(tipoAtendimento) {
    fetch(`/api/atendimento/${tipoAtendimento}`, { method: "POST" })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Erro ao carregar os detalhes do paciente.");
            }
        })
        .then((data) => {
            document.getElementById("tipoAtendimento").textContent = data.tipoAtendimento || "Desconhecido";
            document.getElementById("numeroPaciente").textContent = data.numeroPaciente || "N/A";
            document.getElementById("posicaoNaFila").textContent = data.posicaoNaFila ? `${data.posicaoNaFila}º` : "N/A";
            document.getElementById("sala").textContent = data.sala || "N/A";
            document.getElementById("medico").textContent = data.medico || "N/A";
        })
        .catch((error) => {
            console.error("Erro ao carregar os detalhes do paciente:", error);
            alert("Não foi possível carregar os detalhes do paciente.");
        });
}

/*------------------------------ EVENTOS ------------------------------*/
document.addEventListener("DOMContentLoaded", () => {
    if (document.body.contains(document.getElementById("btnConsulta"))) {
        configurarPaginaInicial();
    } else if (document.body.contains(document.getElementById("tipoAtendimento"))) {
        configurarPaginaFila();
    }
});
document.getElementById('formLogin')?.addEventListener('submit', function (e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    fetch('/api/auth/login', { // Corrigindo o endpoint
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, senha })
    })
        .then(response => {
            if (response.ok) {
                alert('Login bem-sucedido!');
                window.location.href = 'area-medica.html'; // Redireciona para a área médica
            } else {
                alert('Email ou senha incorretos!');
            }
        })
        .catch(error => console.error('Erro ao fazer login:', error));
});
async function chamarProximoPaciente() {
    try {
        const response = await fetch('/api/atendimento/proximo');
        if (response.ok) {
            const data = await response.json();
            document.getElementById('paciente-detalhes').textContent =
                `Paciente: ${data.nome}, Situação: ${data.situacao}, Número: ${data.numeroPaciente}`;
        } else {
            document.getElementById('paciente-detalhes').textContent =
                "Erro ao buscar paciente. Tente novamente.";
        }
    } catch (error) {
        document.getElementById('paciente-detalhes').textContent =
            "Erro de conexão com o servidor.";
    }
    document.addEventListener('DOMContentLoaded', () => {
        const nomeMedicoElement = document.getElementById('nomeMedico');
        const filaElement = document.querySelector('.overflow-x-auto');
        const pacientesAtendidosElement = document.getElementById('pacientesAtendidos');

        // Buscar o nome do médico logado
        async function carregarNomeMedico() {
            try {
                const response = await fetch('/api/medico/atual');
                if (response.ok) {
                    const medico = await response.json();
                    nomeMedicoElement.textContent = medico.nome || 'Médico';
                } else {
                    nomeMedicoElement.textContent = 'Médico não identificado';
                }
            } catch (error) {
                console.error('Erro ao carregar nome do médico:', error);
                nomeMedicoElement.textContent = 'Erro ao carregar nome';
            }
        }

        // Buscar fila de pacientes em espera
        async function carregarFilaPacientes() {
            try {
                const response = await fetch('/api/pacientes/fila');
                if (response.ok) {
                    const pacientes = await response.json();

                    // Limpar conteúdo anterior
                    filaElement.innerHTML = `
                    <table class="w-full">
                        <thead>
                            <tr>
                                <th class="text-left">Número</th>
                                <th class="text-left">Situação</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${pacientes.map(paciente => `
                                <tr>
                                    <td>${paciente.numeroPaciente}</td>
                                    <td>${paciente.situacao || 'Em Espera'}</td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                `;
                } else {
                    filaElement.innerHTML = '<p>Não foi possível carregar a fila de pacientes</p>';
                }
            } catch (error) {
                console.error('Erro ao carregar fila de pacientes:', error);
                filaElement.innerHTML = '<p>Erro ao carregar fila de pacientes</p>';
            }
        }

        // Buscar pacientes já atendidos
        async function carregarPacientesAtendidos() {
            try {
                const response = await fetch('/api/pacientes/atendidos');
                if (response.ok) {
                    const pacientes = await response.json();

                    // Limpar conteúdo anterior
                    pacientesAtendidosElement.innerHTML = pacientes.map(paciente => `
                    <div class="bg-blue-50 p-4 rounded-lg shadow-md flex justify-between items-center">
                        <span class="font-semibold text-blue-600">Número: ${paciente.numeroPaciente}</span>
                        <span class="text-green-600 font-semibold">Atendido</span>
                    </div>
                `).join('');
                } else {
                    pacientesAtendidosElement.innerHTML = '<p>Não foi possível carregar pacientes atendidos</p>';
                }
            } catch (error) {
                console.error('Erro ao carregar pacientes atendidos:', error);
                pacientesAtendidosElement.innerHTML = '<p>Erro ao carregar pacientes atendidos</p>';
            }
        }

        // Função para chamar próximo paciente
        async function chamarProximoPaciente() {
            try {
                const response = await fetch('/api/atendimento/proximo', { method: 'POST' });
                if (response.ok) {
                    // Recarregar as listas após chamar o próximo paciente
                    await Promise.all([
                        carregarFilaPacientes(),
                        carregarPacientesAtendidos()
                    ]);
                    alert('Próximo paciente chamado com sucesso!');
                } else {
                    alert('Não foi possível chamar o próximo paciente');
                }
            } catch (error) {
                console.error('Erro ao chamar próximo paciente:', error);
                alert('Erro de conexão ao chamar próximo paciente');
            }
        }

        // Adicionar evento de clique para chamar próximo paciente
        const botaoChamarProximo = document.querySelector('button');
        if (botaoChamarProximo) {
            botaoChamarProximo.addEventListener('click', chamarProximoPaciente);
        }

        // Carregar dados iniciais
        carregarNomeMedico();
        carregarFilaPacientes();
        carregarPacientesAtendidos();
    });
    async function carregarNomeMedico() {
        try {
            const email = localStorage.getItem('medicoEmail'); // Assumindo que você salva o email no login
            const response = await fetch(`/api/medicos/atual?email=${email}`);
            if (response.ok) {
                const medico = await response.json();
                nomeMedicoElement.textContent = medico.nome || 'Médico';
            } else {
                nomeMedicoElement.textContent = 'Médico não identificado';
            }
        } catch (error) {
            console.error('Erro ao carregar nome do médico:', error);
            nomeMedicoElement.textContent = 'Erro ao carregar nome';
        }
    }
    async function carregarFilaPacientes() {
        try {
            const response = await fetch('/api/pacientes/fila');
            if (response.ok) {
                const pacientes = await response.json();
                const filaElement = document.getElementById('fila-pacientes');
                filaElement.innerHTML = `
        <table class="w-full">
          <thead>
            <tr>
              <th class="text-left">Número</th>
              <th class="text-left">Situação</th>
            </tr>
          </thead>
          <tbody>
            ${pacientes.map(paciente => `
              <tr>
                <td>${paciente.numeroPaciente}</td>
                <td>${paciente.situacao || 'Em Espera'}</td>
              </tr>
            `).join('')}
          </tbody>
        </table>
      `;
            } else {
                filaElement.innerHTML = '<p>Não foi possível carregar a fila de pacientes</p>';
            }
        } catch (error) {
            console.error('Erro ao carregar fila de pacientes:', error);
            filaElement.innerHTML = '<p>Erro ao carregar fila de pacientes</p>';
        }
    }

    async function carregarPacientesAtendidos() {
        try {
            const response = await fetch('/api/pacientes/atendidos');
            if (response.ok) {
                const pacientes = await response.json();
                const pacientesAtendidosElement = document.getElementById('pacientes-atendidos');
                pacientesAtendidosElement.innerHTML = pacientes.map(paciente => `
        <div class="bg-blue-50 p-4 rounded-lg shadow-md flex justify-between items-center">
          <span class="font-semibold text-blue-600">Número: ${paciente.numeroPaciente}</span>
          <span class="text-green-600 font-semibold">Atendido</span>
        </div>
      `).join('');
            } else {
                pacientesAtendidosElement.innerHTML = '<p>Não foi possível carregar pacientes atendidos</p>';
            }
        } catch (error) {
            console.error('Erro ao carregar pacientes atendidos:', error);
            pacientesAtendidosElement.innerHTML = '<p>Erro ao carregar pacientes atendidos</p>';
        }
    }

    async function chamarProximoPaciente() {
        try {
            const response = await fetch('/api/atendimento/proximo', { method: 'POST' });
            if (response.ok) {
                // Recarregar as listas após chamar o próximo paciente
                await Promise.all([
                    carregarFilaPacientes(),
                    carregarPacientesAtendidos()
                ]);
                alert('Próximo paciente chamado com sucesso!');
            } else {
                alert('Não foi possível chamar o próximo paciente');
            }
        } catch (error) {
            console.error('Erro ao chamar próximo paciente:', error);
            alert('Erro de conexão ao chamar próximo paciente');
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        carregarFilaPacientes();
        carregarPacientesAtendidos();

        const botaoChamarProximo = document.querySelector('button');
        if (botaoChamarProximo) {
            botaoChamarProximo.addEventListener('click', chamarProximoPaciente);
        }
    });
}
/*------------------------------ HOME ------------------------------*/
function redirecionar(tipoAtendimento) {
    window.location.href = `fila.html?tipo=${tipoAtendimento}`;
}

function obterParametroUrl(nome) {
    const params = new URLSearchParams(window.location.search);
    return params.get(nome);
}

function configurarPaginaInicial() {
    document.getElementById("btnConsulta").addEventListener("click", () => redirecionar("consulta"));
    document.getElementById("btnEmergencia").addEventListener("click", () => redirecionar("emergencia"));
    document.getElementById("btnColeta").addEventListener("click", () => redirecionar("coleta"));
}

/*------------------------------ FILA ------------------------------*/

function configurarPaginaFila() {
    const tipoAtendimento = obterParametroUrl("tipo") || "Desconhecido";
    document.getElementById("tipoAtendimento").textContent = tipoAtendimento;
    carregarDetalhesPaciente(tipoAtendimento);
}

function carregarDetalhesPaciente(tipoAtendimento) {
    fetch(`/api/atendimento/${tipoAtendimento}`, { method: "POST" })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("Erro ao carregar os detalhes do paciente.");
            }
        })
        .then((data) => {
            document.getElementById("tipoAtendimento").textContent = data.tipoAtendimento || "Desconhecido";
            document.getElementById("numeroPaciente").textContent = data.numeroPaciente || "N/A";
            document.getElementById("posicaoNaFila").textContent = data.posicaoNaFila ? `${data.posicaoNaFila}º` : "N/A";
            document.getElementById("sala").textContent = data.sala || "N/A";
            document.getElementById("medico").textContent = data.medico || "N/A";
        })
        .catch((error) => {
            console.error("Erro ao carregar os detalhes do paciente:", error);
            alert("Não foi possível carregar os detalhes do paciente.");
        });
}

function carregarFila() {
    fetch('/api/atendimento/fila', { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            const tabela = document.getElementById('tabelaFila');
            tabela.innerHTML = ''; // Limpa a tabela
            data.forEach(paciente => {
                const linha = `
                    <tr>
                        <td class="border border-blue-300 p-4">${paciente.numeroPaciente}</td>
                        <td class="border border-blue-300 p-4 text-blue-600 font-semibold">${paciente.situacao}</td>
                    </tr>
                `;
                tabela.innerHTML += linha;
            });
        })
        .catch(error => console.error('Erro ao carregar fila:', error));
}

function carregarAtendidos() {
    fetch('/api/atendimento/atendidos', { method: 'GET' })
        .then(response => response.json())
        .then(data => {
            const listaAtendidos = document.getElementById('pacientesAtendidos');
            listaAtendidos.innerHTML = ''; // Limpa a lista
            data.forEach(paciente => {
                const div = `
                    <div class="bg-blue-50 p-4 rounded-lg shadow-md flex justify-between items-center">
                        <span class="font-semibold text-blue-600">Número: ${paciente.numeroPaciente}</span>
                        <span class="text-green-600 font-semibold">Atendido</span>
                    </div>
                `;
                listaAtendidos.innerHTML += div;
            });
        })
        .catch(error => console.error('Erro ao carregar pacientes atendidos:', error));
}

function chamarProximo() {
    fetch('/api/atendimento/chamarProximo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao chamar próximo paciente");
            }
            return response.json();
        })
        .then(data => {
            alert(`Próximo paciente: ${data.numeroPaciente}`);
            carregarFila(); // Atualiza a tabela de pacientes
            carregarAtendidos(); // Atualiza a lista de atendidos
        })
        .catch(error => {
            alert(`Erro: ${error.message}`);
        });
}

/*------------------------------ EVENTOS ------------------------------*/
document.addEventListener("DOMContentLoaded", () => {
    carregarFila(); // Atualiza a tabela de pacientes ao carregar a página
    carregarAtendidos(); // Atualiza a lista de pacientes atendidos

    const chamarProximoBtn = document.getElementById("chamarProximoBtn");
    if (chamarProximoBtn) {
        chamarProximoBtn.addEventListener("click", chamarProximo);
    }
});

document.getElementById('formLogin')?.addEventListener('submit', function (e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, senha })
    })
        .then(response => {
            if (response.ok) {
                alert('Login bem-sucedido!');
                window.location.href = 'area-medica.html'; // Redireciona para a área médica
            } else {
                alert('Email ou senha incorretos!');
            }
        })
        .catch(error => console.error('Erro ao fazer login:', error));
});
fetch(`/api/atendimento/tipo/${tipoAtendimento}`)
    .then(response => response.json())
    .then(data => {
        console.log(data); // Inspecione os dados recebidos
        // Atualize o HTML conforme necessário
    })
    .catch(error => console.error(error));

// Função para carregar a fila de pacientes e atualizar a tabela
function carregarFila() {
    fetch('/api/atendimento/fila', { method: 'GET' })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao buscar a fila de pacientes.");
            }
            return response.json();
        })
        .then(data => {
            const tabela = document.getElementById('tabelaFila'); // Seleciona o <tbody>
            tabela.innerHTML = ''; // Limpa o conteúdo anterior

            // Itera sobre os pacientes e adiciona as linhas na tabela
            data.forEach(paciente => {
                const linha = document.createElement('tr'); // Cria uma linha <tr>

                // Coluna número
                const colunaNumero = document.createElement('td');
                colunaNumero.className = "border border-blue-300 p-4"; // Adiciona classes para estilização
                colunaNumero.textContent = paciente.numeroPaciente; // Preenche com o número do paciente
                linha.appendChild(colunaNumero);

                // Coluna situação
                const colunaSituacao = document.createElement('td');
                colunaSituacao.className = "border border-blue-300 p-4 text-blue-600 font-semibold";
                colunaSituacao.textContent = paciente.situacao || "Em Espera"; // Preenche com a situação
                linha.appendChild(colunaSituacao);

                // Adiciona a linha à tabela
                tabela.appendChild(linha);
            });
        })
        .catch(error => {
            console.error('Erro ao carregar a fila:', error);
        });
}

// Função para chamar o próximo paciente e atualizar a fila
function chamarProximo() {
    fetch('/api/atendimento/chamarProximo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao chamar próximo paciente");
            }
            return response.json();
        })
        .then(data => {
            alert(`Próximo paciente chamado: ${data.numeroPaciente}`);
            carregarFila(); // Atualiza a tabela após chamar o próximo paciente
        })
        .catch(error => {
            console.error('Erro ao chamar próximo paciente:', error);
            alert(`Erro: ${error.message}`);
        });
}

// Adiciona eventos e carrega dados ao carregar a página
document.addEventListener("DOMContentLoaded", () => {
    carregarFila(); // Atualiza a tabela de pacientes ao carregar a página

    const chamarProximoBtn = document.getElementById("chamarProximoBtn");
    if (chamarProximoBtn) {
        chamarProximoBtn.addEventListener("click", chamarProximo);
    }
});
