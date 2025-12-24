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
