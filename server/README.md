# Votação 

O projeto Votação foi realizado para simular um caso de uso em que eu, enquanto usuário, posso abrir sessões de votações com tempos determinados para finalização. 
Cada sessão possui um código único no formato ####-####-####, no qual quem tiver acesso a este código poderá votar "Sim" ou "Não" na votação.
A qualquer momento, os votadores e o dono da sessão podem visualizar o progresso e obter métricas simples como: o percentual de votos "Sim" e "Não"; a quantidade de pessoas que votaram, etc. 
Cada usuário (member) poderá votar uma única vez por sessão.

## Pré-requisitos
- Java 17;
- Spring boot 3.3.0;
- GraalVM;
- PostgreSQL.
  
### Bibliotecas/Frameworks usados
- jjwt: para manipulação de tokens;
- flyway: para controlar as migrações no banco de dados;
- openapi: para documentar a aplicação usando Swagger UI;
- lombok: para diminuir o boilerplate do projeto;
- junit 5: para realização dos testes automatizados;
- Testcontainer: para realizar teste integrado com maior fidelidade da aplicação, mantendo o postgreSQL como DBMS e não usando um banco em memória como o H2;
- Spring Boot Pacotes: Logging, Hibernate Validator, Security, JPA, DevTools, entre outros (detalhados no build.gradle).

## Como subir a aplicação

1. Clone o repositório:
   ```bash
     git clone https://github.com/VitorManoelLP/desafio-votacao.git
   ```
2. No arquivo server/.env tem uma variável POSTGRES_VOLUME, coloque nela uma pasta para ser usada como volume do container postgreSQL. **Crie um pasta somente para esse propósito**. 
3. Na pasta _server_ há um _docker compose_ que sobe um _container PostgreSQL_ e a própria aplicação, basta entrar na pasta e digitar:
   ```bash
     docker compose up
   ```
4. Acesse a URL <http://localhost:8080/swagger-ui/index.html> para ver todos os _endpoints_ disponíveis.
   Lembre-se de que apenas os _endpoints_ de "Auth" podem ser usados sem a necessidade de um _token_ JWT.
   Para os demais será necessário vincular o _token_ no próprio Swagger.
   
## Estrutura do projeto:

- **.gitignore**: Lista de arquivos e pastas a serem ignorados pelo Git;
- **src/**: Diretório contendo o código-fonte do projeto;
- **tests/**: Diretório contendo os testes do projeto.

### Estrutura interna (dentro da main)

- **/app**: Responsável por armazenar dados mais internos do sistema como serviços, repositórios, interfaces e classes utilitárias;
- **/domain**: Usado para armazenar apenas classes de domìnio e DTOs (package payload);
- **/enums**: Armazenar enums;
- **/exception**: Armazenar exceções personalizadas;
- **/Infra**: Usada para armazenar classes de configurações e controladores;
- **/resources/db/migrations**: Usado para guardar as _migrations_ do banco, no qual o _flyway_ usará para executá-las no momento em que a aplicação subir.

## Arquitetura

Foi optado por usar a comunicação de serviços e controladores via interface para ajudar no versionamento do código, sem a necessidade de mudar grandes pontos no projeto em caso dele escalar. 
A inspiração para essa decisão veio da Arquitetura Limpa e da Arquitetura Hexagonal.

