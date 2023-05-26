# webflux-examples

## Spring RSocket
- protocolo tcp/ websocket
- diferente o modo http do webflux, este possui alguns modos a mais, alem do request /response:
  - request stream -> envio uma solicitação tenho várias respostas (mensagens)
  - bi-directional streaming -> tenho uma varias solicitações e varias respostas (mensagens)
  - fire e forget -> tenho uma solicitação e sem reposta

### Funcionamento
- criamos um RSocket
- existe alguns tipos que podemos sobreescrever, como:
 - request response (com retorno)
 - fireAndForget (sem retorno)
 - request stream (requisição com vários retornos)
 - request-channel (varias requisições com vários retornos)
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