# curso-quarkus
Curso Quarkus Udemy

Listando opções para importação quarkus
mvn quarkus:list-extensions

Adicionando extenções uteis quarkus
mvn quarkus:add-extensions -Dextensions="orm-panache, jdbc-mysql, resteasy-jsonb"

setx /M PATH "C:\Progra~1\Java\graalvm-ce-java11-21.1.0\bin;%PATH%"
setx /M JAVA_HOME "C:\Progra~1\Java\<graalvm-ce-java11-21.1.0"

Curso Quarkus
http://localhost:8080/q/swagger-ui/ - Cadastro Cliente e Pratos
http://localhost:8081/q/swagger-ui/ - Marketplace
http://localhost:8180/auth/ - Keycloak
http://localhost:9090/ - Prometheus
http://localhost:3000/ - Grafana
http://localhost:8161/ - Artemis ActiveMQ  
http://localhost:9000/ - Kafka
http://localhost:5601/app/kibana - Kibana - Para Dashboards


Inclusão de dependencias: via terminal 
#Cadastro
mvn quarkus:add-extension -Dextensions="jdbc-postgres" - Conexão ao banco
mvn quarkus:add-extension -Dextensions="orm-panache" - similiar ao spring-jpa
mvn quarkus:add-extension -Dextensions="resteasy-jsonb" - faz o bind com o json para nos microserviços
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-openapi" - codumentação
mvn quarkus:add-extension -Dextensions="hibernate-validator" - validação para as entidades, vai ser comsumida pela ORM
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-jwt" - para comunicar com o keycloak que fara a geração do token
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-opentracing" - para comunicar com o jeager que fara o monitoramento das requisições Tracing
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-metrics" - Para adicionar as métricas para o prmetheus e grafana
mvn quarkus:add-extension -Dextensions="flyway" - Para versionar o banco de dados;
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-reactive-messaging-amqp" - Enviar mensagens para activeMQ, relativo aos restaurantes adicionados.

#Marketplace
mvn quarkus:add-extension -Dextensions="resteasy-mutiny" - Programação para reatividade, faz o resteasy ficar reativo
mvn quarkus:add-extension -Dextensions="jdbc-postgres" - Conexão ao banco
mvn quarkus:add-extension -Dextensions="flyway" - Para versionar o banco de dados;
mvn quarkus:add-extension -Dextensions="pg-client" - Para a conexão ao banco postgres reativo
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-openapi" - codumentação
mvn quarkus:add-extension -Dextensions="resteasy-jsonb" - faz o bind com o json para nos microserviços
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-reactive-messaging-amqp" - Enviar mensagens para activeMQ.
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-reactive-messaging-kafka" - Adicionando kafka

#Pedido
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-openapi" - codumentação
mvn quarkus:add-extension -Dextensions="resteasy-jsonb" - faz o bind com o json para nos microserviços
mvn quarkus:add-extension -Dextensions="mongodb-panache" - Conexão ao banco
mvn quarkus:add-extension -Dextensions="io.quarkus:quarkus-smallrye-reactive-messaging-kafka" - Adicionando kafka


#Testes
TesteContainers - Faz os testes nos containers
Database Rider - Ajuda a simular o cenário de testes (tipo mokito)
Approval Tests - Melhoria no funlçao do Asserts (JUnit)


#Comandos:
mvn quarkus:dev - Inciar aplicação em modo desenvolvimento

#Prometheus:
docker build -f Dockerfile.prometheus -t prometheus-ifood .


##### Dicas configuração Prometheus e Grafana ##########
não. Você deve remover o "network: host" do docker-compose.yaml; ao invés disso, você vai expor a porta padrão do prometheus igual os outros serviços:
  prometheus_ifood:
    container_name: prometheus_ifood
    image: prometheus-ifood
    ports:
      - 9090:9090

No arquivo prometheus.yaml(~3:00 do video 18), há uma configuração "targets". coloque-a como targets: [host.docker.internal:8080], isso fará o container 
do Prometheus apontar para a máquina host na porta 8080(ou seja, para a instância do quarkus que vc vai subir).



No grafana vai ser mais ou menos a mesma coisa. Remova o network: "host" e exponha a porta padrão no docker-compose.yaml:
  grafana_ifood:
    container_name: grafana_ifood
    image: grafana/grafana:7.1.1
    ports:
      - 3000:3000
No vídeo 19 aos 7min, é mostrado a "URL" de conexão. Nesse caso você vai usar http://prometheus_ifood:9090. Você também pode usar host.docker.internal:9090, 
visto que expomos essa porta no host usando o código anterior.