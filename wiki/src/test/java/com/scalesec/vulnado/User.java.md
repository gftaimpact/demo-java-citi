# User.java: Gerenciamento de Usuários

## Visão Geral
O código é responsável pelo gerenciamento de usuários em um sistema, incluindo a criação de tokens de autenticação, verificação de autenticação e recuperação de informações do usuário a partir de um banco de dados PostgreSQL.

## Fluxo do Processo
```mermaid
graph TD
    A[User] --> B["token(secret)"]
    A --> C["assertAuth(secret, token)"]
    A --> D["fetch(un)"]
    B --> E["Gera token JWT"]
    C --> F["Verifica token JWT"]
    D --> G["Busca usuário no banco de dados"]
```

## Insights
- A classe `User` possui três atributos: `id`, `username` e `hashedPassword`.
- A classe `User` possui três métodos principais: `token`, `assertAuth` e `fetch`.
- O método `token` gera um token JWT para o usuário.
- O método `assertAuth` verifica a autenticidade de um token JWT.
- O método `fetch` recupera um usuário do banco de dados PostgreSQL.
- O método `fetch` é vulnerável a ataques de injeção SQL, pois a entrada do usuário é inserida diretamente na consulta SQL.

## Dependências
```mermaid
graph LR
    User.java --- |"Usa"| io.jsonwebtoken.Jwts
    User.java --- |"Usa"| io.jsonwebtoken.JwtParser
    User.java --- |"Usa"| io.jsonwebtoken.SignatureAlgorithm
    User.java --- |"Usa"| io.jsonwebtoken.security.Keys
    User.java --- |"Usa"| javax.crypto.SecretKey
    User.java --- |"Acessa"| Postgres
```
- `io.jsonwebtoken.Jwts` : Usado para construir e verificar tokens JWT.
- `io.jsonwebtoken.JwtParser` : Usado para analisar tokens JWT.
- `io.jsonwebtoken.SignatureAlgorithm` : Usado para definir o algoritmo de assinatura para tokens JWT.
- `io.jsonwebtoken.security.Keys` : Usado para gerar chaves secretas para tokens JWT.
- `javax.crypto.SecretKey` : Usado para representar chaves secretas para tokens JWT.
- `Postgres` : Classe que fornece conexão com o banco de dados PostgreSQL.

## Manipulação de Dados (SQL)
- `users`: A tabela `users` é acessada para recuperar informações do usuário. A operação SQL realizada é SELECT.

## Vulnerabilidades
- Injeção SQL: O método `fetch` é vulnerável a ataques de injeção SQL, pois a entrada do usuário é inserida diretamente na consulta SQL sem qualquer forma de sanitização ou parametrização. Isso pode permitir que um atacante execute consultas SQL arbitrárias.
