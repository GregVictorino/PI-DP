# 📚 Biblioteca — Sistema de Locação de Livros

Projeto Integrador desenvolvido no curso de **Análise e Desenvolvimento de Sistemas (ADS)** — 3º Semestre.

Sistema web completo para gerenciamento de uma biblioteca, com controle de acervo, locações, clientes e relatórios visuais.

---

## 🚀 Funcionalidades

### 👤 Administrador
- Login com controle de acesso por perfil
- Cadastro e gerenciamento de **livros** (CRUD completo)
- Cadastro e gerenciamento de **clientes**
- Registro e controle de **locações**
- Devolução de livros com atualização automática do estoque
- Dashboard com **gráficos** de status das locações e livros por gênero
- Tabela de locações recentes

### 📖 Cliente
- Acesso ao **catálogo de livros** com busca e filtros
- Solicitação de locação diretamente pelo portal
- Visualização do histórico de locações (ativas, devolvidas, atrasadas)
- Estatísticas pessoais de uso

### 🌐 Catálogo Público
- Página pública sem necessidade de login
- Busca por título, autor, gênero e disponibilidade
- Modal com detalhes completos do livro

### ⚙️ Automações
- Detecção automática de **locações atrasadas** toda meia-noite
- Controle de estoque em tempo real (decrementa ao locar, incrementa ao devolver)

---

## 🛠️ Tecnologias Utilizadas

### Back-end
| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.3.5 | Framework principal (inclui Tomcat, Scheduler) |
| Spring Boot Starter Web | — | Servidor HTTP e serving dos arquivos estáticos |
| Spring Boot Starter Data JPA | — | Persistência de dados com Hibernate (ORM) |
| Spring Boot Starter Validation | — | Validação de campos nos DTOs (`@NotBlank`, `@NotNull`) |
| H2 Database | — | Banco de dados em memória para desenvolvimento |
| PostgreSQL | — | Banco de dados para produção |
| Maven | — | Gerenciamento de dependências e build |

### Front-end
| Tecnologia | Uso |
|------------|-----|
| HTML5 | Estrutura das páginas |
| CSS3 | Estilização e responsividade |
| JavaScript (ES6+) | Interatividade e consumo da API |
| Chart.js | Gráficos do dashboard |
| Fetch API | Comunicação com o back-end |

### Testes
| Tecnologia | Uso |
|------------|-----|
| JUnit 5 | Framework de testes unitários |
| Mockito | Mock de dependências |

---

## 📁 Estrutura do Projeto

```
demo/
├── src/
│   ├── main/
│   │   ├── java/Biblioteca/demo/
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java      # Cria admin padrão na inicialização
│   │   │   │   └── LocacaoScheduler.java     # Detecta atrasos automaticamente
│   │   │   ├── controller/                   # Endpoints REST (/api/...)
│   │   │   ├── dto/                          # Objetos de transferência de dados
│   │   │   ├── exception/                    # Tratamento de erros customizado
│   │   │   ├── model/                        # Entidades JPA (Livro, Usuario, Locacao)
│   │   │   ├── repository/                   # Interfaces Spring Data JPA
│   │   │   ├── service/                      # Regras de negócio
│   │   │   └── DemoApplication.java          # Ponto de entrada
│   │   └── resources/
│   │       ├── static/                       # Frontend (servido em localhost:8080)
│   │       │   ├── index.html                # Tela de login
│   │       │   ├── dashboard.html            # Painel do administrador
│   │       │   ├── cliente.html              # Portal do cliente
│   │       │   ├── catalogo.html             # Catálogo público
│   │       │   ├── css/style.css             # Estilos globais
│   │       │   └── js/api.js                 # Funções de comunicação com a API
│   │       └── application.properties        # Configurações da aplicação
│   └── test/
│       └── java/Biblioteca/demo/
│           ├── service/
│           │   ├── LivroServiceTest.java     # 5 testes unitários do serviço de livros
│           │   └── LocacaoServiceTest.java   # 5 testes unitários do serviço de locações
│           └── DemoApplicationTests.java     # Teste de carregamento do contexto
└── pom.xml
```

---

## ▶️ Como Rodar Localmente

### Pré-requisitos
- Java 21+
- Maven (ou usar o `./mvnw` incluso no projeto)
- IntelliJ IDEA (recomendado)

### Passo a passo

1. **Clone o repositório**
```bash
git clone https://github.com/GregVictorino/PI-DP.git
cd PI-DP/demo
```

2. **Rode a aplicação**
```bash
./mvnw spring-boot:run
```
Ou pelo IntelliJ: abra a pasta `demo` e clique em ▶️ Run.

3. **Acesse no navegador**

| Página | URL | Acesso |
|--------|-----|--------|
| Login | http://localhost:8080 | Público |
| Catálogo público | http://localhost:8080/catalogo.html | Público |
| Painel do administrador | http://localhost:8080/dashboard.html | Admin |
| Portal do cliente | http://localhost:8080/cliente.html | Cliente |
| Console H2 (banco dev) | http://localhost:8080/h2-console | Dev |

4. **Login padrão (admin)**
```
Email: admin@biblioteca.com
Senha: admin123
```

### Rodando os testes
```bash
./mvnw test
```
Resultado esperado: **11 testes, 0 falhas**.

---

## 🔌 Endpoints da API

### Usuários
| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/api/usuarios/login` | Autenticação (retorna dados do usuário) |
| `PUT` | `/api/usuarios/resetar-senha` | Redefinir senha (esqueci a senha) |
| `GET` | `/api/usuarios` | Listar todos os usuários |
| `GET` | `/api/usuarios/{id}` | Buscar usuário por ID |
| `POST` | `/api/usuarios` | Cadastrar novo usuário |
| `PUT` | `/api/usuarios/{id}` | Atualizar usuário |
| `DELETE` | `/api/usuarios/{id}` | Excluir usuário |

### Livros
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/livros` | Listar todos os livros |
| `GET` | `/api/livros/{id}` | Buscar livro por ID |
| `POST` | `/api/livros` | Cadastrar livro |
| `PUT` | `/api/livros/{id}` | Atualizar livro |
| `DELETE` | `/api/livros/{id}` | Excluir livro |

### Locações
| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/api/locacoes` | Listar todas as locações |
| `GET` | `/api/locacoes/{id}` | Buscar locação por ID |
| `GET` | `/api/locacoes/ativas` | Listar apenas locações ativas |
| `GET` | `/api/locacoes/usuario/{usuarioId}` | Listar locações de um cliente |
| `POST` | `/api/locacoes` | Criar nova locação |
| `PUT` | `/api/locacoes/{id}/devolver` | Registrar devolução |
| `DELETE` | `/api/locacoes/{id}` | Excluir locação |

---

## 🗄️ Banco de Dados

O projeto usa bancos diferentes dependendo do ambiente:

### Desenvolvimento (padrão ao rodar localmente)
- **H2 Database** — banco em memória, sobe junto com a aplicação, sem configuração extra
- Os dados são **resetados** toda vez que a aplicação é reiniciada
- Console visual disponível em `http://localhost:8080/h2-console`

```
JDBC URL:  jdbc:h2:mem:biblioteca
Usuário:   sa
Senha:     (vazio)
```

### Produção (deploy)
- **PostgreSQL** via **Supabase**
- As configurações estão comentadas no `application.properties`, prontas para ativar
- Basta substituir `HOST`, `usuário` e `senha` pelos dados do Supabase

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

---

## 📝 Observações

- O banco de dados é **H2 em memória** no ambiente de desenvolvimento — os dados são resetados ao reiniciar a aplicação.
- O usuário administrador padrão é criado automaticamente na primeira inicialização.
- Para produção, configurar as variáveis de ambiente do PostgreSQL no `application.properties`.
