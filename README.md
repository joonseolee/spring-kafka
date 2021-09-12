# spring-kafka

다른 사람들이 만들어놓은 예제들을 따라치면서 파악하기 위한 용도

## example 1 ~ 4

1,2,3,4는 [링크](https://github.com/spring-projects/spring-kafka) 를 통해서 거의 동일하게 만들어보았는데  
트랜잭션 예제인 3번같은경우에는 `producer` 가 `transaction` 를 지원하지않아서  
실제로 실행이 되진않는데 하기위해선 아예 생성자를 만들때 넣어줘야할꺼같지만 (외부에서 조작불가)  
아직 `Kafka` 가 초보라서 이건 스킵하면서 진행했다.

## tcademy tutorial

`tacademy` 에서 짧게 `kafka` 에 대해 설명하는 [강좌](https://youtu.be/ozxVgaqGNhM) 같은게 있다.  
그것들을 예제로 만들어보았는데 소스는 위에꺼보다 더 적다.  
테스트코드 형태로 추가할 예정

## 카프카, 데이터플랫폼의 최강자

카프카에 대해 기본적인 것들은 여기저기 많이 설명되어있지만 실제로 어떻게 구현하고 구성을 잡아야할지 도무지 감이 안잡혀서  
책을 빌려서 공부하는중...

### 리눅스 명령어

이 책에서는 리눅스 명령어 간간히 나오는데  
항상 검색하면서 쓰고 까먹고 쓰고 까먹고하다보니..외울겸 기록해둠

1. 심볼릭 설정

`apache-zookeeper-3.6.3-bin` 이라는 폴더를 `zookeeper` 라는 이름으로 명명 설정

```bash
ln -s apache-zookeeper-3.6.3-bin zookeeper
```

```bash
drwxr-xr-x@ 11 bobo  staff   352B  9  8 19:54 apache-zookeeper-3.6.3-bin
lrwxr-xr-x   1 bobo  staff    26B  9  8 19:36 zookeeper -> apache-zookeeper-3.6.3-bin
drwxr-xr-x   5 bobo  staff   160B  9  8 19:48 zookeeper-cluster
```

2. 통신여부 확인

각 쥬키퍼별로 또는 카프카별로 서로 서버간 통신여부를 확인하기 위해 커맨드 실행  
일단 예시로써 쥬키퍼 2개를 도커로 띄우고 `zoo2` 에서 `zoo3` 연결되는지만 확인

```bash
bobo@localhost ~/D/core> docker inspect fb9 | grep 'Gateway'
            "Gateway": "",
            "IPv6Gateway": "",
                    "Gateway": "172.21.0.1",
                    "IPv6Gateway": "",

root@zoo2:/apache-zookeeper-3.7.0-bin# nc -v 172.21.0.1 2183
Connection to 172.21.0.1 2183 port [tcp/*] succeeded!
```

### 설치방법

`apache zookeeper` 를 압축파일로 다운받아 쉘스크립트로 실행하라고 책에 나와있는데..  
굳이 그럴필요없이 딱 3개로 `docker-compose`형태로 공식으로 지원해주고있어서 [이곳](https://hub.docker.com/_/zookeeper)에서 설치

```bash
user@loo ~/D/d/zookeeper> docker-compose up -d
Docker Compose is now in the Docker CLI, try `docker compose up`

Starting zookeeper_zoo1_1 ... done
Creating zookeeper_zoo3_1 ... done
Creating zookeeper_zoo2_1 ... done
user@loo ~/D/d/zookeeper> docker ps
CONTAINER ID   IMAGE       COMMAND                  CREATED          STATUS         PORTS                                                  NAMES
38e5fa8a7ed6   zookeeper   "/docker-entrypoint.…"   10 seconds ago   Up 4 seconds   2888/tcp, 3888/tcp, 8080/tcp, 0.0.0.0:2182->2181/tcp   zookeeper_zoo2_1
fb947328192f   zookeeper   "/docker-entrypoint.…"   10 seconds ago   Up 2 seconds   2888/tcp, 3888/tcp, 8080/tcp, 0.0.0.0:2183->2181/tcp   zookeeper_zoo3_1
c5b36c552f02   zookeeper   "/docker-entrypoint.…"   2 minutes ago    Up 5 seconds   2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp, 8080/tcp   zookeeper_zoo1_1
user@loo ~/D/d/zookeeper>
```

파프카는 `wurstmeister/kafka` 도커 이미지를 사용해서 올렸다.  
사실 이것만 `docker-compose` 로 올리면 `zookeeper` 로 올라가게되어 굳이 위에꺼 할필요는 없음

```bash
roo@localhost ~/D/m/h/rv [SIGINT]> docker search kafka
NAME                                    DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
wurstmeister/kafka                      Multi-Broker Apache Kafka Image                 1425                 [OK]
spotify/kafka                           A simple docker image with both Kafka and Zo…   414                  [OK]
sheepkiller/kafka-manager               kafka-manager                                   208                  [OK]
kafkamanager/kafka-manager              Docker image for Kafka manager                  142                  
ches/kafka                              Apache Kafka. Tagged versions. JMX. Cluster-…   117                  [OK]
```

카프카 `consumer` 랑 `producer` 를 터미널로 실행시킬려면 아래 커맨드 실행

```bash
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic test --from-beginning

kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic test
```