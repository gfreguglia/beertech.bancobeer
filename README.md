![Yoda Coins Logo](https://raw.githubusercontent.com/gss-patricia/yoda-coins-beertech/main/src/assets/logo-letters.svg?raw=true&sanitize=true "Yoda Coins Logo")
# Challenge POC - Lúpulo + Yoda Coins

## Links importantes:
* Yoda Coins (Front end): https://beertech-lupulo-yoda.herokuapp.com/
* Lúpulo Contas (Back end): https://beertech-lupulo-conta.herokuapp.com/ 
* Notificação: https://beertech-lupulo-notification.herokuapp.com/
* API DOC (Swagger): https://beertech-lupulo-conta.herokuapp.com/swagger-ui.html

* Stack técnico / diagrama de fluxo: https://miro.com/app/board/o9J_khBO-ws=/
* Alterações propostas: https://docs.google.com/document/d/1DgpQnl18hYJxOg_SYzeUklbYJgSyRseuH7N3Zr8ujaE/edit
* ***Banco de dados:***

* Source Backend: https://github.com/gfreguglia/beertech.bancobeer
* Source Frontend: https://github.com/gss-patricia/yoda-coins-beertech


### Endpoints:
#### 1. Conta: 
##### 1a. Manutenção: 
* Cria nova conta: POST https://beertech-lupulo-conta.herokuapp.com/conta
* Atualizar dados de conta: PATCH https://beertech-lupulo-conta.herokuapp.com/conta/{uuid}
* Troca senha: POST https://beertech-lupulo-conta.herokuapp.com/conta/trocar-senha
* Enviar e-mail com link para redefinir senha: POST https://beertech-lupulo-conta.herokuapp.com/conta/pedido-resetar-senha
* Retorna dados de usuário através do token de reset de senha: POST https://beertech-lupulo-conta.herokuapp.com/conta/pedido-resetar-senha
##### 1b. Consulta dados cadastrais
* Consulta paginada de contas: GET https://beertech-lupulo-conta.herokuapp.com/conta
* Consulta única conta: GET https://beertech-lupulo-conta.herokuapp.com/conta/{uuid}
* Consulta de conta por e-mail: GET https://beertech-lupulo-conta.herokuapp.com/conta?email={email}
* Consulta de conta por cnpj: GET https://beertech-lupulo-conta.herokuapp.com/cnpj?email={cnpj}
##### 1c. Consulta de dados fincanteiros
* Consulta saldo da conta: GET https://beertech-lupulo-conta.herokuapp.com/conta/{uuid}/saldo
* Consulta paginada dos últimos lançamentos: GET https://beertech-lupulo-conta.herokuapp.com/conta/{uuid}/operacao
* Consulta paginada dos extrato: GET https://beertech-lupulo-conta.herokuapp.com/conta/{uuid}/extrato


#### 2. Operações financeiras: 
* Adiciona uma nova Operação de Saque ou Depósito: POST https://beertech-lupulo-conta.herokuapp.com/operacao
* Adiciona uma nova Transferência: POST https://beertech-lupulo-conta.herokuapp.com/transferencia

#### 3. Autenticação:
* Autenticação: POST https://beertech-lupulo-conta.herokuapp.com/authenticate

~~_____________________________~~
# Instruções para subir o ambiente (necessita atualizar):
~~API http://127.0.0.1:8080~~

~~RabbitMQ http://127.0.0.1:15672~~

~~## Para subir o ambiente completo:~~
~~Abra um terminal na raiz do projeto e execute~~

~~./gradlew build docker~~

~~docker-compose up -d~~

~~## Para subir o ambiente desenvolvimento:~~
~~docker-compose up -d rabbitmq~~