# Desafio-Meetime

## 🎯 Objetivo
O desafio proposto consiste em desenvolver uma API REST utilizando Spring Boot para realizar a integração com a API da HubSpot, implementando o fluxo de autenticação via OAuth 2.0 (Authorization Code Flow), criação de contatos via API e recebimento de notificações por webhook.

## Principais endpoints:

- **🔐 Geração da URL de autorização**:

```GET /oauth/auth-url```: Retorna a URL que o usuário deve acessar para autorizar a aplicação a acessar sua conta HubSpot via OAuth 2.0.
  
- **🔁 Callback OAuth 2.0**:
  
```GET /oauth/callback?code=```: Endpoint para o qual a HubSpot redireciona após o usuário autorizar o aplicativo. O código recebido é trocado por um token de acesso (e um refresh token), que é armazenado na aplicação.

> ℹ️ Nota: atualmente, o token é armazenado em memória. Em uma aplicação real, o ideal seria persistir os tokens em um banco de dados ou cache seguro.

- **👤 Criação de Contatos**:
  
```POST /api/v1/contacts```: Cria um novo contato no CRM da HubSpot via API. Antes da criação, a aplicação verifica se já existe um contato com o mesmo email para evitar duplicações.

### Corpo da requisição
```
{
  "name": "Teste",
  "email": "teste@example.com"
}
```
### Resposta

```<id do contato criado>```

- **📬 Recebimento de Webhook**:
  
```POST /api/v1/webhook```: Recebe notificações de webhooks do tipo "contact.creation" enviados pela HubSpot quando novos contatos são criados.

## Principais Exceções Tratadas

| Código HTTP | Descrição |
| ----------- | --------- |
| 400        | Bad Request     |
| 401        | Unauthorized (OAuth ausente)     |
| 404        | Not Found (Contato inexistente)     |
| 409        | Conflict (Contato duplicado)     |
| 429        | Too Many Requests (rate limit)     |
| 500        | 	Internal Server Error     |

## 📦 Bibliotecas Utilizadas

1. **Spring Web e WebFlux**: Suporte a APIs REST e WebClient reativo.
2. **Lombok**: Reduz o boilerplate da aplicação.
3. **Spring DevTools**: Facilidade de desenvolvimento..
4. **Commons Codec**:  Conversão de hash para hex (usado na verificação de webhooks).
5. **Junit5 e mockito**: Testes unitários com mock de dependências.
6. **Spring Dotenv**: Carregamento automático de variáveis a partir de ```.env```.
7. **Resilience4j**: Implementa rate limiting de forma declarativa.

## 🛠️ Instruções de Utilização

Pré-requisitos 
1. Conta de desenvolvedor HubSpot: https://developers.hubspot.com/
2. Aplicativo criado no painel do HubSpot Developer

### Configuração do App HubSpot

1. No Dashboard do desenvolvedor, vá em Aplicativos e clique em Criar Aplicativo.
2. Preencha os campos de nome e descrição.
3. Em "Autenticação", configure a URL de redirecionamento:
```http://localhost:8080/oauth/callback```
4. Em "Escopos", adicione:
```
crm.objects.contacts.read
crm.objects.contacts.write
```

### Configuração da Aplicação

1. Crie um arquivo .env com as seguintes variáveis:
```
HUBSPOT_CLIENT_ID=<seu client id>
HUBSPOT_CLIENT_SECRET=<seu client secret>
HUBSPOT_SCOPES=crm.objects.contacts.write%20oauth%20crm.objects.contacts.read
```
2. Use o ngrok para expor o localhost:
```ngrok http 8080```
3. No app da HubSpot, configure o webhook:
- URL: ```https://<ngrok-url>/api/v1/webhook```
- Evento: ```contact.creation```

## Fluxo de Autorização OAuth 2.0

1. Faça uma requisição GET para:
```http://localhost:8080/oauth/auth-url```
2. Copie e acesse a URL retornada no navegador.
3. Após o login e autorização na HubSpot, o navegador será redirecionado para:
```http://localhost:8080/oauth/callback?code=...```
4. A aplicação processa esse **code**, obtém os tokens e os armazena.

## Testando a Criação de Contato

### Endpoint:
**POST** ```http://localhost:8080/api/v1/contacts```

#### Corpo da Requisição
```
{
  "name": "teste",
  "email": "teste@example.com"
}
```

#### Respostas

**STATUS: 201 (CREATED)**
```
123456789 <id do contato criado>
```

**STATUS 409 (CONFLICT)**
```
{
  "exceptionClass": "com.meetime.desafio.config.exceptions.ConflictException",
  "httpStatus": "CONFLICT",
  "message": "Existe um conflito em sua requisição: Contato com email 'teste@example.com' já existe",
  "thrownAt": "2025-03-30T12:58:49.9242896"
}
```

