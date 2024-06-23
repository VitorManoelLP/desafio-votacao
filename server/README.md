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

Optei por usar a comunicação de serviços e controladores via interface para facilitar o versionamento do código, evitando a necessidade de grandes mudanças no projeto à medida que ele escale. A decisão foi inspirada pela Arquitetura Limpa e pela Arquitetura Hexagonal.

### Estrutura dos Serviços

Os serviços foram separados em pequenos contextos, diferentemente da clássica arquitetura MVC na qual toda a lógica de um domínio ficaria centralizada em um único serviço (como `SessionService` ou `VoteService`). Ao invés, adotei uma abordagem mais próxima do DDD (Domain-Driven Design) para estruturar os serviços em domínios por casos de uso. Essa abordagem permite um código mais enxuto e simples por serviço, resultando em mais classes porém com unidades lógicas menores e testes automatizados focados, facilitando a modificação e o incremento.

### Escolha do Banco de Dados

Optei por usar PostgreSQL devido à simplicidade da regra de negócio que requer uma estrutura concisa e que respeite os fundamentos do ACID. O PostgreSQL foi escolhido por sua robustez e conformidade com os princípios ACID, garantindo a integridade dos dados, e com uma velocidade de escrita melhor.

### Autenticação

A autenticação poderia ser implementada de maneira mais simplificada usando Keycloak, OAuth2 ou soluções similares. No entanto, como a estrutura é vinculada apenas ao backend, decidi implementar algo mais manual para exemplificar o uso do Spring Security puro.

### Uso de _Cache_

Poderia utilizar _caches_ para buscas simples, por exemplo na apuração de sessões fechadas, ou no caso de ter uma API externa, como nas sugestões do "Extra" vinculadas ao README principal do projeto. No entanto, optei por não implementar _caches_ no momento, considerando que um bom _cache_ eleva o custo do projeto e, atualmente, não vi a necessidade disso. Futuramente poderia usar um _cache_ simples do Spring ou até mesmo o Redis.

### Logging

Implementei uma boa quantidade de logs de debug espalhados pelo código para facilitar a depuração usando ferramentas como Graylog. Além disso, o Spring Admin foi configurado para permitir a troca do nível de log de INFO para DEBUG conforme necessário, evitando poluir os logs da aplicação com informações desnecessárias.

### Migration

Costumo utilizar dois tipos de migrations: Liquibase e Flyway. Neste projeto, optei pelo Flyway por ser mais simples e atender às necessidades. O Flyway é eficiente para criar tabelas e índices com arquivos .sql fáceis de gerenciar, enquanto o Liquibase oferece maior compatibilidade, algo não necessário aqui.

### GraalVM

Utilizei o GraalVM para aumentar a resiliência da aplicação, permitindo uma recuperação mais rápida em caso de falhas.

### Possíveis Melhorias

Para um ambiente de produção, algumas melhorias seriam recomendadas:
- Uso de um framework de autenticação, como Keycloak, para maior segurança e Single Sign-On (SSO).
- Implementação de _caches_ para reduzir conexões desnecessárias ao banco de dados, como na apuração de sessões fechadas (avaliar o custo).
- Uso de WebSockets para notificar o dono e os membros sobre novos votos ou fechamento de sessões em tempo real.
- Implementação de uma ferramenta como Jasper para gerar relatórios mais robustos e detalhados em PDF, conforme necessidade do cliente.

## Conclusão

Meu objetivo foi buscar a maior simplicidade possível, evitando tornar o projeto excessivamente robusto com uma arquitetura limpa completa, _caches_ avançados e _frameworks_ como Keycloak para autenticação.
