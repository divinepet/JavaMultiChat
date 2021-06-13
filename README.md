# JavaMultiChat
## Инструкция по запуску:
### 1. Запустить  сервер на PostgreSQL
### 2. В папке SocketServer:
> java -jar target/socket-server.jar --port=8080
3. В папке SocketClient:
> java -jar target/socket-client.jar --port-server=8080
4. Доступные команды:
Регистрация:
> signUp
Вход:
> signIn
