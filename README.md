# 🚀 AI Recruitment Platform

Plataforma de reclutamiento inteligente que utiliza IA (OpenAI) para analizar hojas de vida, generar pruebas técnicas adaptativas y clasificar candidatos mediante un ranking combinado.

## 📋 Descripción

El sistema automatiza el proceso de selección de candidatos:
1. El **reclutador** crea ofertas laborales con habilidades requeridas
2. El **candidato** sube su CV en PDF → el sistema lo analiza con OpenAI
3. El candidato presenta una **prueba técnica** generada por IA (10 preguntas)
4. El sistema calcula un **ranking**: `FINAL = (0.6 × CV_Score) + (0.4 × Tech_Score)`

## 🏗️ Arquitectura

8 microservicios independientes con Java 17 + Spring Boot WebFlux:

| Servicio | Puerto | Función |
|----------|--------|---------|
| api-gateway | 8080 | Enrutamiento, JWT, autorización |
| user-service | 8081 | Usuarios y autenticación |
| job-offer-service | 8082 | CRUD ofertas laborales |
| cv-processing-service | 8083 | Carga y extracción PDF |
| ai-analysis-service | 8084 | Análisis CV con OpenAI |
| scoring-service | 8085 | Cálculo CV Score |
| technical-test-service | 8086 | Pruebas técnicas |
| evaluation-service | 8087 | Evaluación automática |
| ranking-service | 8088 | Ranking final |
| frontend | 3001 | React JS (nginx) |

**Infraestructura:** Eureka (8761), Config Server (8888), Kafka (9092), PostgreSQL (5432), MongoDB (27017)

## ⚙️ Requisitos Previos

- **Java 17+** (Amazon Corretto recomendado)
- **Maven 3.8+**
- **Docker Desktop** (con Docker Compose)
- **Node.js 18+** (solo para desarrollo del frontend)
- **API Key de OpenAI** (para análisis de CV y generación de pruebas)

## 🚀 Puesta en Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/ai-recruitment-platform.git
cd ai-recruitment-platform
```

### 2. Configurar variables de entorno

Crear archivo `.env` en la raíz del proyecto:

```env
OPENAI_API_KEY=sk-tu-api-key-aqui
```

### 3. Compilar todos los microservicios

```bash
mvn clean package -DskipTests
```

> En Windows, si JAVA_HOME no apunta al JDK correcto:
> ```powershell
> $env:JAVA_HOME = "C:\Program Files\Amazon Corretto\jdk17.0.19_10"
> mvn clean package -DskipTests
> ```

### 4. Levantar con Docker Compose

```bash
docker-compose up --build
```

Espera 2-3 minutos para que todos los servicios arranquen y se registren en Eureka.

### 5. Acceder a la aplicación

| URL | Descripción |
|-----|-------------|
| http://localhost:3001 | Frontend (aplicación) |
| http://localhost:8761 | Eureka Dashboard |

## 👤 Uso de la Aplicación

### Registrar un Reclutador
1. Ir a http://localhost:3001/register
2. Marcar "Soy reclutador"
3. Ingresar código: `GFT2026`
4. Completar registro

### Registrar un Candidato
1. Ir a http://localhost:3001/register
2. Completar registro (sin marcar reclutador)

### Flujo del Reclutador
1. Login → Crear Oferta Laboral (nombre, habilidades, seniority)
2. Ver Ranking de candidatos por oferta

### Flujo del Candidato
1. Login → Subir CV (PDF, máx 10MB)
2. Ir a Prueba Técnica → Seleccionar oferta → Responder 10 preguntas
3. Enviar respuestas (solo una vez por oferta)

## 🛑 Detener el Sistema

```bash
docker-compose down
```

Para eliminar también los volúmenes (bases de datos):
```bash
docker-compose down -v
```

## 📁 Estructura del Proyecto

```
ai-recruitment-platform/
├── api-gateway/           # Spring Cloud Gateway + JWT
├── user-service/          # Autenticación y usuarios
├── job-offer-service/     # CRUD ofertas laborales
├── cv-processing-service/ # Upload PDF + extracción texto
├── ai-analysis-service/   # Integración OpenAI + MongoDB
├── scoring-service/       # Cálculo CV Score
├── technical-test-service/# Pruebas técnicas con IA
├── evaluation-service/    # Evaluación automática
├── ranking-service/       # Ranking final
├── eureka-server/         # Service Discovery
├── config-server/         # Configuración centralizada
├── common-events/         # Eventos Kafka compartidos
├── frontend/              # React JS + Vite + nginx
├── infrastructure/        # Scripts SQL de inicialización
├── docker-compose.yml     # Orquestación de contenedores
├── pom.xml                # Parent POM (Maven multi-módulo)
└── .env                   # Variables de entorno (no commitear)
```

## 🔧 Tecnologías

- **Backend:** Java 17, Spring Boot 3.2, WebFlux (reactivo)
- **Arquitectura:** Clean Architecture + Hexagonal
- **Frontend:** React 18, Vite, Axios, React Router
- **Bases de datos:** PostgreSQL 16, MongoDB 7
- **Mensajería:** Apache Kafka
- **Infraestructura:** Docker, Eureka, Config Server, Spring Cloud Gateway
- **IA:** OpenAI GPT-4o-mini
- **Seguridad:** JWT, BCrypt

## 📝 Notas

- La primera vez que un candidato solicita la prueba técnica, puede tardar 10-15 segundos mientras OpenAI genera las preguntas.
- El ranking se actualiza automáticamente después de que el candidato envía la prueba (pipeline Kafka asíncrono).
- Si el sistema no responde al inicio, espera 2-3 minutos — los microservicios necesitan registrarse en Eureka.

## 👨‍💻 Autor

**Camilo Sandoval** - Universidad Manuela Beltrán - Práctica Empresarial GFT (2026)
