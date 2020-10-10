# Legacy POC - Lúpulo

## Para subir o banco de dados:
Abra um terminal em **src/main/resources/sql**

`docker-compose up`

Em um novo terminal: 

`docker exec -it sql_db_1 bash`

`mysql -uroot -proot`

`use lupulo_db;`

Copie e cole o conteúdo de **src/main/resources/sql/create-tables.sql**  
