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

이후 `apache kafka` 설치
