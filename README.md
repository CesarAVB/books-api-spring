# Livros API Backend

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen)
![Build Status](https://github.com/CesarAVB/books-api-spring/workflows/CI%20Pipeline/badge.svg)
![Branches](.github/badges/branches.svg)
![Tests](https://img.shields.io/badge/tests-45%20passing-success)

<!-- César -->

## 🚀 Tech Stack
- Java 21
- Spring Boot 3.5.9
- H2 Database (dev)
- PostgreSQL (prod)
- Maven
- JUnit 5 + Mockito
- Spring Boot Actuator

## 🏗️ Estrutura do Projeto
```
src/main/java/br/com/sistema/livros/
├── controller/     # Controladores REST
├── service/        # Lógica de negócio
├── repository/     # Acesso a dados (JPA)
├── model/          # Entidades JPA
├── dto/            # Data Transfer Objects
├── config/         # Configurações
└── exception/      # Tratamento de exceções
```

## 🔧 Como executar

### Pré-requisitos
- Java 21
- Maven 3.8+

### Executar localmente
```bash
./mvnw spring-boot:run
```

### Acessar endpoints
- API: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- Health Check: http://localhost:8080/actuator/health

## 📍 Roadmap
- [x] Setup inicial do projeto
- [x] Configuração de profiles (dev/prod)
- [x] Spring Boot Actuator
- [ ] Configuração global de exceções
- [ ] Implementação de entidades e repositories
- [ ] Testes unitários
- [ ] Testes de integração
- [ ] CI/CD com GitHub Actions

## 📝 Licença
Projeto de estudos - Livre para uso educacional
```

---

### **4. Criar .gitignore (se não tiver)**

Crie na raiz: `.gitignore`
```
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### Eclipse ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### VS Code ###
.vscode/

### Mac ###
.DS_Store

### H2 Database ###
*.db

### Logs ###
*.log