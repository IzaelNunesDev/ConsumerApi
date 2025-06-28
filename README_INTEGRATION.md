# Integração RotaFácil - Android com Backend

## Visão Geral

Este projeto Android foi integrado com o backend oficial do RotaFácil disponível em: **https://projetodepersistenciatp3.onrender.com**

## Funcionalidades Implementadas

### ✅ Autenticação
- **Login de Alunos e Motoristas**: Integração completa com o endpoint `/api/v1/auth/login`
- **Registro de Alunos**: Integração com `/api/v1/auth/register/aluno`
- **Gerenciamento de Token JWT**: Armazenamento seguro usando DataStore
- **Logout**: Limpeza automática dos dados de autenticação

### ✅ Estrutura de Dados
- **DTOs Atualizados**: Estruturas de dados compatíveis com o backend
- **Modelos de Domínio**: Separação clara entre dados da API e lógica de negócio
- **Mapeamento de Respostas**: Conversão automática entre JSON e objetos Kotlin

### ✅ Arquitetura
- **MVVM com Clean Architecture**: Separação de responsabilidades
- **Hilt para Injeção de Dependência**: Gerenciamento de dependências
- **Repository Pattern**: Abstração da camada de dados
- **Jetpack Compose**: Interface moderna e responsiva

## Configuração da API

### URL Base
```kotlin
// NetworkModule.kt
.baseUrl("https://projetodepersistenciatp3.onrender.com/")
```

### Endpoints Principais
- **Login**: `POST /api/v1/auth/login`
- **Registro Aluno**: `POST /api/v1/auth/register/aluno`
- **Usuário Atual**: `GET /api/v1/auth/me`
- **Alunos**: `GET /api/v1/alunos/{id}`
- **Motoristas**: `GET /api/v1/motoristas/{id}`
- **Rotas**: `GET /api/v1/rotas/ativas`
- **Viagens**: `GET /api/v1/viagens/aluno/{alunoId}`

## Estrutura de Resposta do Login

O backend retorna a seguinte estrutura para login:

```json
{
  "access_token": "jwt_token_aqui",
  "token_type": "bearer",
  "user_info": {
    "id": "user_id",
    "nome": "Nome do Usuário",
    "email": "email@exemplo.com",
    "tipo": "aluno" // ou "motorista"
  }
}
```

## Autenticação

### Interceptor HTTP
O `AuthInterceptor` adiciona automaticamente o token JWT em todas as requisições autenticadas:

```kotlin
header("Authorization", "Bearer $token")
```

### Armazenamento Seguro
- **DataStore**: Armazenamento seguro de tokens e dados do usuário
- **Criptografia**: Tokens armazenados de forma segura
- **Limpeza Automática**: Logout remove todos os dados sensíveis

## Telas Implementadas

### 1. Tela de Login (`LoginScreen`)
- Formulário de login com email e senha
- Modo de registro integrado
- Validação de campos
- Feedback visual de loading e erros
- Navegação automática após login

### 2. Tela Principal (`HomeScreen`)
- Dashboard após login
- Informações sobre a integração
- Botão de logout
- Lista de funcionalidades disponíveis

## Como Testar

### 1. Compilar e Executar
```bash
./gradlew assembleDebug
```

### 2. Testar Login
- Use credenciais válidas de um aluno ou motorista
- Verifique se o token é salvo corretamente
- Teste o logout e verifique se os dados são limpos

### 3. Testar Registro
- Registre um novo aluno
- Verifique se o login automático funciona
- Teste com dados inválidos para verificar validações

## Próximos Passos

### Funcionalidades Pendentes
- [ ] Visualização de rotas ativas
- [ ] Acompanhamento de viagens
- [ ] Gestão de frequência
- [ ] Notificações push
- [ ] Mapa com localização em tempo real

### Melhorias Técnicas
- [ ] Cache offline com Room
- [ ] Sincronização de dados
- [ ] Tratamento de erros mais robusto
- [ ] Testes unitários e de integração
- [ ] CI/CD pipeline

## Troubleshooting

### Problemas Comuns

1. **Erro de Conexão**
   - Verifique se a URL da API está correta
   - Confirme se o backend está online
   - Verifique as permissões de internet no AndroidManifest

2. **Erro de Autenticação**
   - Verifique se o token está sendo enviado corretamente
   - Confirme se o token não expirou
   - Teste o endpoint de login diretamente

3. **Erro de Parsing JSON**
   - Verifique se os DTOs correspondem à estrutura da API
   - Confirme se o Moshi está configurado corretamente
   - Verifique os logs do Retrofit para detalhes

## Dependências Principais

```kotlin
// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.11.0")
implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

// Dependency Injection
implementation("com.google.dagger:hilt-android:2.48")
implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

// UI
implementation("androidx.compose:compose-bom:2023.10.01")
implementation("androidx.compose.material3:material3")

// Data Storage
implementation("androidx.datastore:datastore-preferences:1.0.0")
```

## Contato

Para dúvidas sobre a integração, consulte a documentação da API em:
**https://projetodepersistenciatp3.onrender.com/docs** 