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
 
## Serialização/Deserialização
- Por padrão, o WebFlux usa o protocolo baseado em texto HTTP1.1. (Não é apenas um problema do WebFlux. É o protocolo HTTP 1.1 que tem mais de 20 anos)
RSocket / gRPC (HTTP2) usa formato binário. formato binário é mais compacto em comparação com o texto. Portanto, podemos ver uma melhoria significativa no desempenho em alguns casos.

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

### Conexão
- podemos gerenciar e ver a conexão de um cliente ao nosso server
- inclusive podemos fechar a conexão em caso de falha, usando o dispose(), que libera os recursos envolvidos no Mono.
```
    @ConnectMapping
    public Mono<Void> handleConnection(ClientConnectionRequest request, RSocketRequester rSocketRequester) {
        System.out.println("connection setup " + request);
        return request.getSecretKey().equals("password") ? Mono.empty() :
                Mono.fromRunnable(() -> rSocketRequester.rsocketClient().dispose());
    }
```

## Session
- quando temos uma sessao aberta com um stream (comunicação bidirecional)
- caso a conexão seja perdida, uma estratégia de retomar de onde parou é replay strategy ou resume

```
    public void connectionTest() throws InterruptedException {
        var req = this.builder
                .rsocketConnector(c -> c
                        .resume(resumeStrategy())
                        //.reconnect(retryStrategy())
                        )
                .transport(TcpClientTransport.create("localhost", 6566));

        final Flux<ComputationResponseDto> send =  req.route("math.service.table")
                .data(new ComputationRequestDto(5))
                .retrieveFlux(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(send)
                .expectNextCount(1000)
                .verifyComplete();

        Thread.sleep(2000);

    }

    private Resume resumeStrategy() {
        return new Resume()
                .retry(Retry.fixedDelay(2000, Duration.ofSeconds(2))
                        .doBeforeRetry(s ->  System.out.println("resume - retry: " + s.totalRetriesInARow())));
    }
```

## load balance
- podemos utilizar o load balance do lado do servidor, por exemplo usando o nginx como um proxy reverso
- podemos tambem utilizar o load balance do lado do cliente, fazendo uso de um registry e a estratégia de direcionamento

### estratégia de load balance
#### round robin
- Nessa estratégia, os pedidos de clientes são sequencialmente direcionados para os recursos disponíveis em uma ordem circular, em um ciclo contínuo. Por exemplo, se houver três servidores A, B e C, a primeira solicitação será enviada para o servidor A, a segunda para o servidor B, a terceira para o servidor C e a quarta novamente para o servidor A, e assim por diante.

#### wighted
- A estratégia de balanceamento de carga ponderada, ou "Weighted Load Balancing Strategy" em inglês, é uma variação do algoritmo de 
balanceamento de carga Round Robin, onde cada recurso disponível recebe um peso ou valor atribuído para indicar sua capacidade relativa de processamento.

- Nessa estratégia, em vez de distribuir o tráfego de forma igualitária entre os recursos, cada recurso recebe uma porção do tráfego proporcional ao seu peso. 
Recursos com pesos mais altos receberão uma carga maior de solicitações, enquanto recursos com pesos mais baixos receberão uma carga menor. 
Isso permite que recursos mais poderosos ou com maior capacidade processem mais solicitações do que os outros.

- Por exemplo, considere três servidores A, B e C, com pesos atribuídos de 3, 2 e 1, respectivamente. 
Em cada ciclo de distribuição de carga, o servidor A receberá 3 vezes mais solicitações do que o servidor C, 
enquanto o servidor B receberá o dobro das solicitações do servidor C.

- O balanceamento de carga ponderada é útil quando os recursos têm capacidades diferentes ou 
quando é necessário priorizar certos recursos em relação a outros. 
Permite uma alocação mais eficiente de recursos com base em suas capacidades individuais.
No entanto, é importante observar que a escolha dos pesos requer uma compreensão adequada das capacidades dos recursos e do 
padrão de tráfego esperado. Se os pesos não forem atribuídos corretamente, a carga ainda pode ficar desequilibrada. 
Portanto, é essencial ajustar e monitorar os pesos conforme necessário para garantir um balanceamento de carga eficaz.

#### Segurança
```
O funcionamento da criptografia assimétrica envolve os seguintes conceitos:

Chave pública: Cada entidade envolvida no processo de criptografia assimétrica possui um par de chaves: uma chave pública e uma chave privada. A chave pública é amplamente divulgada e pode ser compartilhada com qualquer pessoa. Ela é usada para criptografar os dados que serão enviados ao proprietário da chave privada.

Chave privada: A chave privada é mantida em segredo e é conhecida apenas pelo proprietário. Ela é usada para descriptografar os dados que foram criptografados com a chave pública correspondente. A chave privada não deve ser compartilhada ou divulgada publicamente.

Criptografia: Quando uma entidade deseja enviar uma mensagem para outra entidade usando criptografia assimétrica, ela usa a chave pública do destinatário para criptografar a mensagem. A mensagem criptografada pode ser enviada de forma segura pela rede.

Descriptografia: O destinatário recebe a mensagem criptografada e a descriptografa usando sua chave privada correspondente. Somente a chave privada correta pode desfazer a criptografia e revelar o conteúdo original da mensagem.

Assinaturas digitais: Além da criptografia, as chaves públicas e privadas também são usadas para fornecer autenticidade e integridade aos dados. O remetente pode usar sua chave privada para assinar digitalmente uma mensagem, criando uma assinatura digital única. O destinatário pode usar a chave pública do remetente para verificar a autenticidade da assinatura e garantir que a mensagem não tenha sido alterada desde a assinatura.
```
- para o rsocket temos segurança no nivel de setup e request
  - setup seria no nivel de autenticação
  - request nivel de autorização
