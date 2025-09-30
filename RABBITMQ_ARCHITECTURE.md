# Diagrama de Arquitectura del Sistema de Colas con RabbitMQ

## Flujo del Sistema de Colas de Tareas

```mermaid
graph TD
    A[Scheduler Cron<br/>4:00 PM diario] --> B[Encolar Tarea Principal]
    B --> C[Cola Principal<br/>habit.main.queue]
    C --> D[Procesador Principal<br/>DailyHabitProcessingService]
    D --> E[Consultar Registros<br/>del Día Anterior]
    E --> F{¿Hay registros<br/>sin calificar?}
    F -->|Sí| G[Encolar Tareas Individuales]
    F -->|No| H[Finalizar Procesamiento]
    G --> I[Cola Individual<br/>habit.individual.queue]
    I --> J[Procesadores Individuales<br/>HabitScoreCalculationService]
    J --> K[Calcular Calificación<br/>del Registro]
    K --> L[Guardar en Base de Datos]
    L --> M[Registro Completado]
    
    style A fill:#e1f5fe
    style C fill:#f3e5f5
    style I fill:#f3e5f5
    style D fill:#e8f5e8
    style J fill:#e8f5e8
```

## Componentes del Sistema

```mermaid
graph LR
    subgraph "Spring Boot Application"
        A[HabitScoreScheduler]
        B[HabitQueueService]
        C[DailyHabitProcessingService]
        D[HabitScoreCalculationService]
        E[HabitRecordRepository]
    end
    
    subgraph "RabbitMQ"
        F[Exchange: habit.exchange]
        G[Cola Principal<br/>habit.main.queue]
        H[Cola Individual<br/>habit.individual.queue]
    end
    
    subgraph "Base de Datos"
        I[MongoDB<br/>HabitRecord Collection]
    end
    
    A --> B
    B --> F
    F --> G
    F --> H
    G --> C
    H --> D
    C --> E
    D --> E
    E --> I
```

## Configuración de Colas

```mermaid
graph TD
    A[Exchange: habit.exchange<br/>Type: Topic] --> B[Binding: habit.main]
    A --> C[Binding: habit.individual]
    B --> D[Cola Principal<br/>habit.main.queue<br/>TTL: 5 minutos]
    C --> E[Cola Individual<br/>habit.individual.queue<br/>TTL: 10 minutos]
    
    D --> F[Consumidores: 3-10<br/>Procesamiento Concurrente]
    E --> G[Consumidores: 3-10<br/>Procesamiento Concurrente]
    
    style A fill:#ffeb3b
    style D fill:#4caf50
    style E fill:#4caf50
```

## Flujo de Datos

```mermaid
sequenceDiagram
    participant S as Scheduler
    participant QS as QueueService
    participant MQ as RabbitMQ
    participant DP as DailyProcessor
    participant IP as IndividualProcessor
    participant DB as MongoDB
    
    S->>QS: enqueueDailyHabitProcessing(yesterday)
    QS->>MQ: Send to habit.main.queue
    MQ->>DP: Process daily records
    DP->>DB: Find records by date
    DB-->>DP: Return records list
    DP->>QS: enqueueIndividualTasks(records)
    QS->>MQ: Send to habit.individual.queue
    
    loop For each record
        MQ->>IP: Process individual record
        IP->>DB: Find record by ID
        DB-->>IP: Return record
        IP->>IP: Calculate overall score
        IP->>DB: Save updated record
        DB-->>IP: Confirm save
    end
```

## Características de Escalabilidad

- **Procesamiento Concurrente**: 3-10 consumidores por cola
- **Colas Durable**: Persistencia de mensajes
- **TTL Configurado**: Evita acumulación de mensajes
- **Retry Logic**: Hasta 3 intentos por tarea
- **Dead Letter Queue**: Para mensajes fallidos (implementación futura)

## Ventajas de RabbitMQ

### ✅ **Para Desarrollo Local:**
- **Fácil Instalación**: Un comando con Homebrew
- **Configuración Simple**: Configuración mínima requerida
- **Interfaz Web**: Monitoreo visual en localhost:15672
- **Open Source**: Gratuito y flexible
- **Control Total**: Configuración completa del sistema

### ✅ **Escalabilidad:**
- **Procesamiento Concurrente**: Múltiples consumidores
- **Colas Durable**: Persistencia de mensajes
- **Clúster**: Alta disponibilidad
- **Load Balancing**: Distribución de carga

### ✅ **Monitoreo:**
- **Interfaz Web**: Dashboard completo
- **Métricas**: Estadísticas en tiempo real
- **Logs**: Logging detallado
- **Alertas**: Configuración de alertas

## Comparación con Cloud Tasks

| Característica | RabbitMQ | Cloud Tasks |
|----------------|----------|-------------|
| **Costo** | Gratuito | Pago por uso |
| **Mantenimiento** | Manual | Automático |
| **Escalabilidad** | Manual | Automática |
| **Monitoreo** | Básico | Avanzado |
| **Vendor Lock-in** | No | Sí |
| **Desarrollo Local** | Fácil | Complejo |
| **Producción** | Requiere configuración | Plug & Play |

## Próximos Pasos

1. **Instalar RabbitMQ**: `brew install rabbitmq`
2. **Iniciar RabbitMQ**: `brew services start rabbitmq`
3. **Ejecutar la aplicación**: `mvn spring-boot:run`
4. **Monitorear colas**: http://localhost:15672
5. **Verificar logs**: Revisar logs de la aplicación