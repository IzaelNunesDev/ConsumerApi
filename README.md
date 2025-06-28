# RotaFácil - Aplicativo Android

Aplicativo Android para o sistema de transporte escolar RotaFácil, desenvolvido com as melhores práticas modernas de desenvolvimento Android.

## 🚀 Tecnologias Utilizadas

- **Linguagem:** Kotlin 100%
- **Arquitetura:** MVVM + Clean Architecture
- **UI:** Jetpack Compose
- **Injeção de Dependência:** Hilt
- **Networking:** Retrofit + OkHttp + Moshi
- **Persistência Local:** Room Database
- **Navegação:** Jetpack Navigation Compose
- **Mapas:** Google Maps SDK
- **Push Notifications:** Firebase Cloud Messaging
- **Assincronia:** Coroutines & Flow

## 📱 Funcionalidades

### Para Alunos:
- Login e autenticação
- Visualização de rotas ativas
- Acompanhamento de viagens em tempo real
- Histórico de viagens
- Notificações push

### Para Motoristas:
- Login e autenticação
- Visualização de viagens do dia
- Gerenciamento de viagens (iniciar/finalizar)
- Check-in de frequência dos alunos
- Reportar incidentes
- Rastreamento em tempo real

## 🛠️ Configuração do Projeto

### Pré-requisitos
- Android Studio Hedgehog ou superior
- JDK 11 ou superior
- API do Google Maps configurada
- Projeto Firebase configurado

### 1. Configuração do Google Maps

1. Acesse o [Google Cloud Console](https://console.cloud.google.com/)
2. Crie um novo projeto ou use um existente
3. Ative a Maps SDK for Android
4. Crie uma chave de API
5. Substitua `YOUR_GOOGLE_MAPS_API_KEY` no `AndroidManifest.xml`

### 2. Configuração do Firebase

1. Acesse o [Firebase Console](https://console.firebase.google.com/)
2. Crie um novo projeto
3. Adicione um app Android com o package `com.rotafacil.app`
4. Baixe o arquivo `google-services.json` e substitua o placeholder
5. Ative o Cloud Messaging

### 3. Configuração da API Backend

O app está configurado para conectar com a API FastAPI no endereço:
- **Emulador:** `http://10.0.2.2:8000/`
- **Dispositivo físico:** `http://localhost:8000/`

Para alterar o endereço, edite o arquivo `NetworkModule.kt`.

## 🏗️ Estrutura do Projeto

```
com.rotafacil.app/
├── data/
│   ├── local/          # Room Database
│   ├── remote/         # Retrofit API
│   └── repository/     # Implementações dos repositórios
├── di/                 # Módulos Hilt
├── domain/
│   ├── model/          # Modelos de domínio
│   ├── repository/     # Interfaces dos repositórios
│   └── usecase/        # Casos de uso
└── ui/
    ├── theme/          # Tema e estilos
    ├── navigation/     # Navegação
    ├── screens/        # Telas do app
    └── viewmodel/      # ViewModels
```

## 🔧 Configuração de Desenvolvimento

### 1. Clone o repositório
```bash
git clone <repository-url>
cd ConsumerApi
```

### 2. Abra no Android Studio
- Abra o Android Studio
- Selecione "Open an existing project"
- Navegue até a pasta do projeto e selecione

### 3. Sincronize o projeto
- Aguarde a sincronização do Gradle
- Resolva qualquer dependência faltante

### 4. Configure as chaves de API
- Substitua as chaves no `AndroidManifest.xml` e `google-services.json`
- Configure o endereço da API no `NetworkModule.kt`

### 5. Execute o projeto
- Conecte um dispositivo ou inicie um emulador
- Clique em "Run" no Android Studio

## 📋 Próximos Passos

### Fase 1: Estrutura Base ✅
- [x] Configuração do projeto
- [x] Estrutura de pacotes
- [x] Dependências configuradas
- [x] Módulos Hilt criados
- [x] Modelos de domínio
- [x] Interfaces de repositório

### Fase 2: Implementação da Camada de Dados
- [ ] Implementação dos repositórios
- [ ] Configuração do Room Database
- [ ] Implementação da API Service
- [ ] Mappers completos

### Fase 3: UI e Navegação
- [ ] Tela de login
- [ ] Navegação principal
- [ ] Telas do aluno
- [ ] Telas do motorista

### Fase 4: Funcionalidades Avançadas
- [ ] Rastreamento em tempo real
- [ ] Push notifications
- [ ] Cache offline
- [ ] Testes unitários

## 🧪 Testes

Para executar os testes:
```bash
# Testes unitários
./gradlew test

# Testes instrumentados
./gradlew connectedAndroidTest
```

## 📦 Build e Release

Para gerar um APK de release:
```bash
./gradlew assembleRelease
```

O APK será gerado em: `app/build/outputs/apk/release/`

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Suporte

Para dúvidas ou suporte, entre em contato através de:
- Email: suporte@rotafacil.com
- Issues do GitHub

---

**Desenvolvido com ❤️ para o RotaFácil** 