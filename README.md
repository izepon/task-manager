# 🚀 Task Manager API

API RESTful para gerenciamento de tarefas internas.

O projeto foi construído com foco em praticar desenvolvimento usando boas práticas na construção de software, incluindo uma arquitetura limpa, código testável e um ambiente de desenvolvimento e produção totalmente containerizado com Docker.

## ✨ Features

-   Gerenciamento completo de **Usuários**, **Tarefas** e **Subtarefas** via endpoints REST.
-   Implementação de regras de negócio, como a conclusão de tarefas condicionada ao status de suas subtarefas.
-   API com suporte a filtros combinados e paginação.
-   Documentação de API interativa e completa com **Swagger/OpenAPI**.
-   Estrutura de projeto escalável seguindo o padrão **Layered Architecture**.
-   Gerenciamento de schema de banco de dados robusto e versionado com **Flyway**.
-   Ambiente 100% containerizado com **Docker** e **Docker Compose**, garantindo consistência entre desenvolvimento e produção.

---

## 🛠️ Tecnologias Utilizadas

-   **Java 17**
-   **Spring Boot 3**
-   **Spring Data JPA** & **Hibernate**
-   **PostgreSQL** (Banco de Dados Relacional)
-   **Flyway** (Database Migration)
-   **Maven** (Gerenciador de Dependências)
-   **Lombok**
-   **Docker** & **Docker Compose**
-   **Springdoc OpenAPI (Swagger)** para documentação da API
-   **JUnit 5** & **Mockito** para testes unitários

---

## ⚙️ Pré-requisitos

Para executar este projeto, você precisará ter instalado:
-   **JDK 17** ou superior
-   **Maven 3.8** ou superior
-   **Docker** e **Docker Compose**

---

## 🚀 Como Executar o Projeto (Docker)

Esta é a forma recomendada para executar a aplicação, pois garante que todo o ambiente (API + Banco de Dados) suba de forma integrada e configurada.

### 1. Empacote a Aplicação
Primeiro, é necessário criar o arquivo `.jar` da aplicação, que será inserido na imagem Docker. Na raiz do projeto, execute:
```bash
mvn clean package ou diretamente pela sua IDE.
```

### 2. Inicie o Ambiente com Docker Compose
Com o Docker Desktop em execução, use o Docker Compose para construir a imagem da API e iniciar todos os containers.

```bash
docker compose up --build
```

O comando --build força a reconstrução da imagem da API, garantindo que as últimas alterações do código sejam aplicadas.

Para rodar em segundo plano, use ```docker compose up --build -d```.

Após a execução, todo o ambiente estará no ar. A API estará disponível em http://localhost:8080.

## 📚 Documentação e Testes da API

### 1. Swagger UI (Documentação Interativa)

Com a aplicação em execução, a documentação completa e interativa da API está disponível no seu navegador através do Swagger UI. Você pode usar esta interface para visualizar e testar todos os endpoints.

URL: http://localhost:8080/swagger-ui.html

### 2.Postman (Testes Manuais)
Uma collection do Postman foi criada para facilitar os testes manuais e a validação do fluxo completo da API. Para usá-la:

Importe o arquivo **[Task Manager API.postman_collection.json](https://github.com/user-attachments/files/21694109/Task.Manager.API.postman_collection.json)** no seu Postman.

Crie um ambiente no Postman com a variável baseUrl definida como http://localhost:8080.

Execute as requisições na ordem sugerida (criar usuário, depois tarefa, depois subtarefa) para testar o fluxo completo.

## 🧪 Executando os Testes Automatizados

O projeto possui uma suíte de testes unitários para a camada de serviço e de controller. Para executá-los, use o seguinte comando Maven:

```bash
mvn test
```

## 🗺️ Estrutura dos Endpoints da API

### **Users**
| Método | Endpoint       | Descrição                 |
|--------|---------------|---------------------------|
| POST   | `/users`       | Cria um novo usuário.     |
| GET    | `/users/{id}`  | Busca um usuário por ID.  |

---

### **Tasks**
| Método | Endpoint               | Descrição                                    |
|--------|-----------------------|----------------------------------------------|
| POST   | `/tasks`               | Cria uma nova tarefa para um usuário.        |
| GET    | `/tasks`               | Lista tarefas com filtros e paginação.       |
| PATCH  | `/tasks/{id}/status`   | Atualiza o status de uma tarefa.              |

---

### **Subtasks**
| Método | Endpoint                               | Descrição                                    |
|--------|---------------------------------------|----------------------------------------------|
| POST   | `/tasks/{taskId}/subtasks`            | Cria uma nova subtarefa para uma tarefa.     |
| GET    | `/tasks/{taskId}/subtasks`            | Lista todas as subtarefas de uma tarefa.     |
| PATCH  | `/subtasks/{subtaskId}/status`        | Atualiza o status de uma subtarefa.          |

---

© [2025] [Jean Carlos Izepon]. Todos os direitos reservados.

Distribuído sob a Licença MIT. Consulte o arquivo LICENSE para mais detalhes.
