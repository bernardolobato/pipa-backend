
# pipa-backend-test
## Rodando a aplicação
### Via Maven
    mvn spring-boot:run
### Via Docker
    docker container run bernardolobato/pipa:latest
## Testando a aplicação

    mvn test
## Considerações
* Assumi que a quantidade de requisições post para incremento dos scores seriam maiores que a quantidade de get para a lista de highscores, portanto, a solucão que adotei foi que a escrita seria mais performatica, salvando as informações em um `ConcurrentHashMap` ([https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html). Essa estrutura de dados tem como foco o casamento entre a performance e a concorrência em uma condição de corrida.
* Realizei testes com três frameworks mvc diferentes tendo como objetivo metrificar as performances: Spring MVC, Javalin, e Quarkus.
* O tempo de resposta para os endpoints acabou sendo muito semelhante entre todos, havendo alguma diferença mais tangivel somente no startup da aplicação, onde o `quarkus`teve o melhor tempo entre os três. Como o tempo de startup de uma aplicação não é tão relevante no contexto do teste (vai iniciar somente uma vez em um ambiente não distribuido), optei pela utilização do Spring MVC com Spring Boot pela minha maior familiariedade e conhecimento deste.
* Mesmo com essa escolha, tomei cuidados de deixar separado em camadas, sendo a camada web apartada da camada de serviços, portanto, caso queiramos testar um novo framework, um trabalho mínimo é necessário.
* A arquitetura que utilizei é um padrão de arquitetura para desenvolvimento de APIS: uma camada de controller, que conecta com uma camada de serviços e transfere dados através de objetos DTO. Não usei objetos de domínios, pois pelo meu julgamento nao seria necessário.
* Não foi usada nenhuma forma de autenticação
* Os testes unitários foram escritos com JUnit.
## A solução
### Adicionando pontos
Para armazenamento da pontuação dos usuários, utilizei uma instância de ConcurrentHashMap, pois, conforme a documentação apresentada, se mostra uma boa opção quando pensamos no casamento performance x multithread. o Método utilizado foi o `compute`. A chave utilizada foi o id do usuário e o valor foi o somatorio dos pontos daquele usuário.
 `this.scores.compute(score.getUserId(), (k, v) -> v == null ? score.getPoints() : v + score.getPoints());`

### Obtendo a posição de um usuário
* Para essa solução criei uma lista ordenada de objetos ScoreFormDTO, que possuem id do usuário e pontuação deste. Para obter a posição de um determinado usuário, implementei um Comparator que busca dentro dessa lista através do `Collections.binarySearch()`
* Um ponto de atenção é que essa lista é ordenada em tempo de execução e armazenada no estado do objeto de serviço.
* Uma vez armazenada, sempre que uma requisição for feita que faça referencia a esta lista ordenada, a mesma lista já armazenada no estado do objeto é retornada, a não ser que um novo post de pontuação seja realizado. Nesse caso a lista precisa ser reordenada. Esse fluxo é controlado através de um atributo dirty, que é setado para true sempre que um novo post é inserido
### Obtendo a lista de maiores pontuadores
* A mesma lógica que foi utilizada para obter a posição de um usuário será utilizada para obter a lista dos maiores pontuadores
* Nesse caso, devolvemos uma sublista com até 20k elementos inseridos na lista.
## Conclusões
* Obtive alguns tempos bons na execução de uma estrutura com mais de 100k elementos (em torno de 5ms), claro, rodando localmente, o que precisamos desconsiderar a latência de rede.
* Quaisquer esclarecimentos estou a disposição