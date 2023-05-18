# webflux-examples

## Spring RSocket
- protocolo tcp/ websocket
- diferente o modo http do webflux, este possui alguns modos a mais, alem do request /response:
  - request stream -> envio uma solicitação tenho várias respostas (mensagens)
  - bi-directional streaming -> tenho uma varias solicitações e varias respostas (mensagens)
  - fire e forget -> tenho uma solicitação e sem reposta