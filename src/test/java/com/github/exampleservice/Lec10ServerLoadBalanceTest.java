package com.github.exampleservice;

import com.github.exampleservice.controller.dto.ComputationRequestDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
})
public class Lec10ServerLoadBalanceTest {

    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void connectionTest() throws InterruptedException {
        var req1 = this.builder.transport(TcpClientTransport.create("localhost", 6566));
        var req2 = this.builder.transport(TcpClientTransport.create("localhost", 6566));

        for (int i =0; i < 50; i++) {
            req1.route("math.service.print").data(new ComputationRequestDto(i)).send().subscribe();
            req2.route("math.service.print").data(new ComputationRequestDto(i)).send().subscribe();

            Thread.sleep(2000);
        }
    }
}
