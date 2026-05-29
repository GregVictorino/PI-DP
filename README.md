# 📚 Biblioteca — Sistema de Locação de Livros

Projeto Integrador desenvolvido no curso de **Análise e Desenvolvimento de Sistemas (ADS)** — 3º Semestre.

Sistema web completo para gerenciamento de uma biblioteca, com controle de acervo, fluxo de solicitação de locações, aprovação pelo administrador, detecção automática de atrasos e relatórios visuais.

---

## 🖥️ Telas do Sistema

### Login
![Login](https://raw.githubusercontent.com/GregVictorino/PI-DP/main/screenshots/login.png)

### Painel do Administrador
![Dashboard](https://raw.githubusercontent.com/GregVictorino/PI-DP/main/screenshots/dashboard.png)

### Solicitações de Locação
![Solicitações](https://raw.githubusercontent.com/GregVictorino/PI-DP/main/screenshots/solicitacoes.png)

### Portal do Cliente
![Cliente](https://raw.githubusercontent.com/GregVictorino/PI-DP/main/screenshots/cliente.png)

---

## 🚀 Funcionalidades

### 👤 Administrador
- Login com controle de acesso por perfil (ADMIN / CLIENTE)
- Cadastro e gerenciamento de **livros** (CRUD completo com capa, gênero, ISBN, descrição)
- Cadastro e gerenciamento de **clientes**
- **Aprovação ou rejeição** de solicitações de locação enviadas pelos clientes
- Registro e controle de **locações** com filtro por status
- **Contador regressivo** de prazo de devolução (verde / laranja / vermelho)
- Devolução de livros com atualização automática do estoque
- **Verificação manual de atrasos** com navegação automática para os registros
- Dashboard com estatísticas e gráficos de status das locações e livros por gênero

### 📖 Cliente
- Acesso ao **catálogo de livros** com busca por título/autor e filtros por gênero e disponibilidade
- **Solicitação de locação** — o admin precisa aprovar antes de ativar
- Visualização do histórico de locações com **contador de dias restantes**
- Identificação visual de locações atrasadas

### ⚙️ Automações
- Detecção automática de **locações atrasadas** toda meia-noite via `@Scheduled`
- Controle de estoque em tempo real (decrementa ao aprovar, incrementa ao devolver)
- Banco populado automaticamente ao iniciar com livros, admin e cliente de demonstração

---

## 🏗️ Design Patterns Utilizados

Este projeto aplica os seguintes padrões de projeto (Design Patterns):

| Padrão | Onde está | Por que foi usado |
|---|---|---|
| **MVC** (Model-View-Controller) | `Controller` → `Service` → HTML | Separa responsabilidades: a tela não conhece a regra de negócio, o Controller não acessa o banco |
| **Repository Pattern** | `LivroRepository`, `UsuarioRepository`, `LocacaoRepository` | Abstrai o acesso ao banco — nenhum SQL manual; queries geradas pelo Spring Data JPA |
| **Service Layer** | `LivroService`, `LocacaoService` | Centraliza toda a lógica de negócio; Controller e Repository não se comunicam diretamente |
| **DTO** (Data Transfer Object) | `LocacaoRequestDTO` | Separa o que entra pela API da entidade que vai ao banco; evita expor campos internos |
| **Command** (CommandLineRunner) | `DataInitializer` | Encapsula a ação de popular o banco ao subir a aplicação; executado uma única vez na inicialização |
| **Scheduler** | `LocacaoScheduler` | Executa a verificação de atrasos automaticamente a cada dia à meia-noite, sem intervenção humana |

---

## 🛠️ Tecnologias Utilizadas

### Back-end
| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.3.5 | Framework principal (inclui Tomcat embutido) |
| Spring Data JPA | — | Persistência com Hibernate (ORM) — sem SQL manual |
| Spring Boot Validation | — | Validação de campos nos DTOs (`@Valid`, `@NotBlank`, `@NotNull`) |
| Spring Boot DevTools | — | Hot reload de arquivos estáticos durante o desenvolvimento |
| H2 Database | — | Banco relacional em memória para desenvolvimento |
| PostgreSQL | — | Banco de dados para produção (configuração pronta em `application.properties`) |
| Maven | — | Gerenciamento de dependências e build |

### Front-end
| Tecnologia | Uso |
|------------|-----|
| HTML5 | Estrutura das páginas |
| CSS3 | Estilização e responsividade |
| JavaScript ES6+ | Interatividade e consumo da API REST via `fetch` |
| Chart.js | Gráficos do dashboard (pizza de status e gêneros) |

### Testes
| Tecnologia | Uso |
|------------|-----|
| JUnit 5 | Framework de testes unitários e de integração |
| Mockito | Mock de dependências nos testes unitários |
| Spring Boot Test | Contexto completo para testes de integração com H2 |

---

## 📁 Estrutura do Projeto

```
demo/
├── src/
│   ├── main/
│   │   ├── java/Biblioteca/demo/
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java       # Popula banco ao iniciar (Command Pattern)
│   │   │   │   └── LocacaoScheduler.java      # Detecta atrasos à meia-noite (Scheduler Pattern)
│   │   │   ├── controller/                    # Endpoints REST — recebem e respondem requisições HTTP
│   │   │   │   ├── LivroController.java
│   │   │   │   ├── LocacaoController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   └── DashboardController.java
│   │   │   ├── dto/                           # Data Transfer Objects (DTO Pattern)
│   │   │   │   └── LocacaoRequestDTO.java
│   │   │   ├── exception/                     # Tratamento de erros customizado
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── model/                         # Entidades JPA (Model)
│   │   │   │   ├── Livro.java
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Locacao.java
│   │   │   │   └── enums/
│   │   │   │       ├── Role.java              # ADMIN, CLIENTE
│   │   │   │       └── StatusLocacao.java     # PENDENTE, ATIVA, DEVOLVIDA, ATRASADA, REJEITADA
│   │   │   ├── repository/                    # Spring Data JPA (Repository Pattern)
│   │   │   ├── service/                       # Regras de negócio (Service Layer Pattern)
│   │   │   │   ├── LivroService.java
│   │   │   │   └── LocacaoService.java
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       ├── static/                        # Frontend servido em localhost:8080
│   │       │   ├── index.html                 # Tela de login
│   │       │   ├── dashboard.html             # Painel do administrador
│   │       │   ├── cliente.html               # Portal do cliente
│   │       │   ├── css/style.css
│   │       │   └── js/api.js                  # Funções de comunicação com a API
│   │       └── application.properties
│   └── test/
│       └── java/Biblioteca/demo/
│           ├── FluxoSolicitacaoIntegrationTest.java  # 4 testes de integração (contexto completo)
│           ├── service/
│           │   ├── LivroServiceTest.java             # 5 testes unitários
│           │   └── LocacaoServiceTest.java           # 7 testes unitários
│           └── DemoApplicationTests.java             # 1 teste de contexto
└── pom.xml
```

---

## ▶️ Como Rodar Localmente

### Pré-requisitos
- Java 21+
- Maven (ou usar o `./mvnw` incluso no projeto)

### Passo a passo

**1. Clone o repositório**
```bash
git clone https://github.com/GregVictorino/PI-DP.git
cd PI-DP/demo
```

**2. Rode a aplicação**
```bash
./mvnw spring-boot:run
```

**3. Acesse no navegador**

| Página | URL |
|--------|-----|
| Login | http://localhost:8080 |
| Painel do administrador | http://localhost:8080/dashboard.html |
| Portal do cliente | http://localhost:8080/cliente.html |
| Console H2 (banco) | http://localhost:8080/h2-console |

**4. Credenciais de acesso**

| Perfil | Email | Senha |
|--------|-------|-------|
| Administrador | admin@biblioteca.com | admin123 |
| Cliente (demo) | joao.silva@email.com | cliente123 |

> O banco é populado automaticamente ao iniciar — admin, cliente e 4 livros já cadastrados.

### Rodando os testes
```bash
./mvnw test
```
Resultado esperado: **17 testes, 0 falhas**.

---

## 🔌 Endpoints da API

### Usuários
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/usuarios/login` | Autenticação |
| `GET` | `/api/usuarios` | Listar todos |
| `POST` | `/api/usuarios` | Cadastrar |
| `PUT` | `/api/usuarios/{id}` | Atualizar |
| `DELETE` | `/api/usuarios/{id}` | Excluir |

### Livros
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/livros` | Listar (com filtros: busca, gênero, disponível) |
| `GET` | `/api/livros/{id}` | Buscar por ID |
| `POST` | `/api/livros` | Cadastrar |
| `PUT` | `/api/livros/{id}` | Atualizar |
| `DELETE` | `/api/livros/{id}` | Excluir |

### Locações
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/locacoes` | Listar todas |
| `GET` | `/api/locacoes/ativas` | Listar ativas |
| `GET` | `/api/locacoes/usuario/{id}` | Locações de um cliente |
| `POST` | `/api/locacoes` | Criar solicitação (status PENDENTE) |
| `PUT` | `/api/locacoes/{id}/aprovar` | Aprovar solicitação → status ATIVA |
| `PUT` | `/api/locacoes/{id}/rejeitar` | Rejeitar solicitação → status REJEITADA |
| `PUT` | `/api/locacoes/{id}/devolver` | Registrar devolução → status DEVOLVIDA |
| `POST` | `/api/locacoes/verificar-atrasos` | Marcar vencidas como ATRASADA |
| `DELETE` | `/api/locacoes/{id}` | Excluir |

### Dashboard
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/dashboard/resumo` | Estatísticas gerais (livros, locações, clientes, pendentes) |

---

## 🗄️ Banco de Dados

### Desenvolvimento (padrão)
- **H2 Database** — banco relacional em memória, sobe junto com a aplicação
- Dados resetados ao reiniciar (banco populado automaticamente pelo `DataInitializer`)
- Console: `http://localhost:8080/h2-console` | URL: `jdbc:h2:mem:biblioteca` | Usuário: `sa` | Senha: *(vazio)*

### Produção
- **PostgreSQL** — configuração pronta e comentada no `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://HOST:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

---

## 👥 Equipe

Desenvolvido por **Greg Victorino** e **Duda** — ADS 3º Semestre.
