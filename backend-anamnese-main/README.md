# CLÍNICA ANAMNESE - BACKEND 

<p>API desenvolvida na linguagem Java, com o framework Spring Boot.</p>

<br/>

## CONFIGURAÇÕES
### INICIAR PROGRAMA LOCALMENTE

É necessário instalar o **JDK (versão 17+)** e o **Maven (versão 3.9+)** - para compilar e iniciar localmente.

<br/>

**Comandos para Iniciar Programa:**
<p><strong>mvn clean</strong> <em>- (opcional, mas recomendado) limpa os arquivos e artefatos gerados na compilação anterior.</em></p>
<p><strong>mvn spring-boot:run</strong> <em>- compila e inicia o programa.</em></p>

<br/>

### BANCO DE DADOS

<p>O banco de dados SQL padrão ao iniciar localmente (modo dev) é o H2 Database - banco local em memória (in-memory).</p>
<p>Após compilar e iniciar o programa novamente, os dados armazenados anteriormente são excluídos.</p>
<p>É possível fazer essa alteração nos arquivos: <strong>application-dev.yaml</strong> e <strong>application-prod.yaml</strong></p>

<br/>

### DOCKER


**Gerar Imagem**

 <p><strong>docker build -t backend-anamnese:x.x .</strong> <em>- x.x é a versão</em>.</p> 

 <br/>

 **Iniciar Container** - Porta 8080

 <p><strong>docker run -d -p 8080:8080 --name backend-container backend-anamnese:x.x</strong> <em>- x.x deve ser igual à versão de imagem gerada</em>.</p> 
