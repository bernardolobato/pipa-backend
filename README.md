
  

# pipa-backend-test

## Rodando a aplicação

### Via Maven

mvnw spring-boot:run

### Via Docker

docker container run -p 8080:8080 bernardolobato/pipa:latest

## Testando a aplicação
    mvnw test

## Executando os endpoints
### Post Score

    curl --location --request POST 'localhost:8080/score' \    
    --header 'Content-Type: application/json' \    
    --data-raw '{    
    "userId":58,    
    "points":10    
    }'

### Get Position

    curl --location --request GET 'localhost:8080/58/position'


### Get highscorelist

    curl --location --request GET 'localhost:8080/highscorelist' 


## Considerações

* Assumi que a quantidade de requisições `post` para incremento dos scores seriam maiores que a quantidade de `get` para a lista de `highscores`, portanto, a solução que adotei foi que a escrita seria mais performática, salvando as informações em um `ConcurrentHashMap` ([https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html). Essa estrutura de dados tem como foco o casamento entre a performance e a consistência em uma condição de corrida.
* Realizei testes com três frameworks mvc diferentes tendo como objetivo metrificar as performances: Spring MVC, Javalin, e Quarkus.
* O tempo de resposta para os endpoints acabou sendo muito semelhante entre todos, havendo alguma diferença mais tangível somente no *startup* da aplicação, onde o `quarkus`teve o melhor tempo entre os três. Como o tempo de *startup* de uma aplicação não é tão relevante no contexto do teste (vai iniciar somente uma vez em um ambiente não distribuído), optei pela utilização do Spring MVC com Spring Boot pela minha maior familiaridade e conhecimento mais aprofundado deste.

* Mesmo com essa escolha, tomei cuidados de deixar separado em camadas, sendo a camada web apartada da camada de serviços, portanto, caso queiramos testar um novo framework, um trabalho mínimo é necessário.

* A arquitetura que utilizei é um padrão de arquitetura para desenvolvimento de APIS: uma camada de *controller*, que conecta com uma camada de serviços e transfere dados através de objetos DTO. Não usei objetos de domínios, pois pelo meu julgamento não seria necessário.

* Não foi usada nenhuma forma de autenticação.

* Os testes unitários foram escritos com JUnit.
* Na alternativa de se utilizar integer sem sinal, optei pela utilização de Long, adicionando uma validação no controller, evitando numeros negativos. 

## A solução

### Adicionando pontos

Para armazenamento da pontuação dos usuários, utilizei uma instância de `ConcurrentHashMap`, pois, conforme a documentação apresentada, se mostra uma boa opção quando pensamos no casamento performance x *multithread*. o Método utilizado foi o `compute`. A chave utilizada foi o id do usuário e o valor foi o somatório dos pontos daquele usuário.

    this.scores.compute(score.getUserId(), (k, v) -> v == null ? score.getPoints() : v + score.getPoints());

  

### Obtendo a posição de um usuário

* Para essa solução criei uma lista ordenada de objetos `ScoreFormDTO`, que possuem id do usuário e pontuação deste. Para obter a posição de um determinado usuário, implementei um Comparator que busca dentro dessa lista através do `Collections.binarySearch()`. O índice desse objeto dentro da lista acaba sendo sua posição

* Um ponto de atenção é que essa lista é ordenada em tempo de execução e armazenada no estado do objeto de serviço.

* Uma vez armazenada, sempre que uma requisição for feita que faça referencia a esta lista ordenada, a mesma lista já processada no estado do objeto é retornada, a não ser que um novo post de pontuação seja realizado. Nesse caso a lista precisa ser reordenada. Esse fluxo é controlado através de um atributo `dirty`, que é setado para true sempre que um novo post é inserido

### Obtendo a lista de maiores pontuadores

* A mesma lógica que foi utilizada para obter a posição de um usuário será utilizada para preencher obter a lista dos maiores pontuadores

* Nesse caso, devolvemos uma `sublista` com até 20k elementos inseridos na lista.
* **ATENÇÃO!** Tanto no endpoint de posicionamento de usuário quanto na listagem de maiores pontuadores, a primeira posição é 0.

## Conclusões

* Obtive alguns tempos bons na execução de uma estrutura com mais de 100k elementos (em torno de 5ms), claro, rodando localmente, o que precisamos desconsiderar a latência de rede.

* Quaisquer esclarecimentos estou a disposição
