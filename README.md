# FIAP Feedback Admin (Microsserviço 4)

Este repositório contém o microsserviço de **Admins** da plataforma de Feedback. Ele é responsável por cadastrar os admins que desejam receber emails referentes aos feedbacks de urgência e aos relatórios.

### Arquitetura da Solução

```mermaid
flowchart LR
Admin((Administrador))

    subgraph MS4["MS4: fiap-feedback-admin"]
        APIGW_Admin["API Gateway<br/>POST /admins/subscription"]
        Lambda_Admin["Lambda<br/>AdminManagementFunction"]
    end

    subgraph Infra["Infra (MS4)"]
        DB_Admins[("DynamoDB<br/>Tabela: Admins")]
    end

    Admin -->|1. POST /admins/subscription| APIGW_Admin
    APIGW_Admin -->|2. Trigger| Lambda_Admin
    Lambda_Admin -->|3. CRUD Admins| DB_Admins

    classDef lambda fill:#f9f,stroke:#333,stroke-width:1px;
    classDef api fill:#fff3b0,stroke:#333,stroke-width:1px;
    classDef db fill:#336699,stroke:#333,stroke-width:1px,color:#fff;

    class APIGW_Admin api;
    class Lambda_Admin lambda;
    class DB_Admins db;
```

## 📦 Como Fazer o Deploy

1.  **Compile o projeto:**
```bash
.\mvnw.cmd clean package -DskipTests
```

2.  **Execute o deploy guiado com base no `samconfig.toml` já existente:**
```bash
sam deploy
```
    

3. **Para deletar os serviços criados da AWS**
```bash
sam delete --stack-name fiap-feedback-admin
```
