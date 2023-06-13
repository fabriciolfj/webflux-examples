# webflux-examples

## RFC 7807
- spring boot 3 suporta o contrato da norma acima, o ProblemDetail, que precisa ser ativado no properties da aplicação

```
spring:
  mvc:
    problemdetails:
      enabled: true
```
- ou extendendo a classe abaixo:
```
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    //...
}
```

## socket
- socket é uma interface de programação que permite a comunicação bidirecional de baixo nível entre dois computadores em uma rede. Ele oferece flexibilidade máxima para enviar e receber dados arbitrários.

## Spring RSocket
- protocolo tcp/ websocket
- diferente o modo http do webflux, este possui alguns modos a mais, alem do request /response:
  - request stream -> envio uma solicitação tenho várias respostas (mensagens)
  - bi-directional streaming -> tenho uma varias solicitações e varias respostas (mensagens)
  - fire e forget -> tenho uma solicitação e sem reposta

### Funcionamento
- criamos um RSocket
- existe alguns tipos que podemos sobreescrever, como:
 - request response (com retorno), exemplo de uso: criar um usuário (recebe mono retorna mono)
 - fireAndForget (sem retorno), exemplo de uso: logar, deletar um usuario enviar um evento (recebe mono e retorna mono void)
 - request stream (requisição com vários retornos), exemplo de uso: procurar resultados, fazer varios updates (recebe mono e retorna um flux)
 - request-channel (varias requisições com vários retornos), exemplo de uso: chat, game e etc. (recebe um flux e retorna um flux)
- abaixo um exemplode classe que o implementa:
```
@Slf4j
public class MathService implements RSocket {

    @Override
    public Mono<Void> fireAndForget(final Payload payload) {
        log.info("receiving: {}", payload.getDataUtf8());
        return Mono.empty();
    }
}

```
- em seguida o registramos em um acceptor, conforme demonstrado abaixo
```
@Slf4j
public class SocketAcceptorImpl implements SocketAcceptor {

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
        log.info("socket acceptor impl-accept method");
        return Mono.fromCallable(MathService::new);
    }
}
```
- por fim o colocamos o acceptor na configuração do server:
```
        final RSocketServer socketServer = RSocketServer.create(new SocketAcceptorImpl());
        final CloseableChannel closeableChannel = socketServer.bindNow(TcpServerTransport.create(6565));

        closeableChannel.onClose().block();
```
- exemplo de envio de mensagem fire e forget
```
        final var payload = DefaultPayload.create("hello world");
        final var mono = this.rSocket.fireAndForget(payload);
```
- lembrando que nesse modelo simples, temos apenas um acceptor registrado no nosso servidor

### Tratativas de erro
- podemos trar os erros de 3 maneiras:
  - defaultIfEmpty -> colocar um valor default se nao atendeu alguma regra
  - swithIfEmpty -> emitir um evento ou error (Mono.error())
  - onErrorReturn -> em caso de erro, emitir um valor, este e para o ouvinte do evento
- um outra forma é utilizar a anotação @MessageExceptionHandler, conforme o exemplo abaixo, que captura as exceptions lançadas como IllegalArgumentException
```
    @MessageExceptionHandler(IllegalArgumentException.class)
    public Mono<Integer> handleException(Exception e) {
        return Mono.just(-1);
    }
```