package com.github.exampleservice;

import com.github.exampleservice.controller.dto.ComputationRequestDto;
import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.loadbalance.RoundRobinLoadbalanceStrategy;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;

import java.util.List;

//load balance do lado do cliente utiliza um registry para qual maquina bater
@SpringBootTest
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
})
public class Lec11ClientLoadBalanceTest {

    @Autowired
    private RSocketRequester.Builder builder;

    @Autowired
    private Flux<List<LoadbalanceTarget>> targets;

    //Nessa estratégia, os pedidos de clientes são sequencialmente direcionados para os recursos disponíveis em uma ordem circular, em um ciclo contínuo. Por exemplo, se houver três servidores A, B e C, a primeira solicitação será enviada para o servidor A, a segunda para o servidor B, a terceira para o servidor C e a quarta novamente para o servidor A, e assim por diante.
    @Test
    public void clientTest() throws InterruptedException {
        var req1 = this.builder.transports(targets, new RoundRobinLoadbalanceStrategy());

        for (int i =0; i < 50; i++) {
            req1.route("math.service.print").data(new ComputationRequestDto(i)).send().subscribe();

            Thread.sleep(2000);
        }
    }
}
