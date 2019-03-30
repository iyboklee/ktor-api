# ktor-api

코틀린 기반 REST API, GraphQL 서비스를 만드는 예제입니다.

가능한 Java 코드를 섞어쓰지 않고 순수하게 코틀린을 사용합니다.

주요 코틀린 컴포넌트는 다음과 같습니다.

* [Ktor - asynchronous Web framework for Kotlin](https://ktor.io/)
* [Exposed - Kotlin SQL Framework](https://github.com/JetBrains/Exposed)
* [kgraphql - Pure Kotlin GraphQL implementation](https://github.com/pgutkowski/KGraphQL)

테스트에 사용된 컴포넌트는 다음과 같습니다.

* Junit5
* [mockito-kotlin - Using Mockito with Kotlin](https://github.com/nhaarman/mockito-kotlin)

## 빌드와 실행

서버를 구동시키기 위한 Main 메소드 구현은 [com.github.iyboklee.server.MainKt](https://github.com/iyboklee/ktor-api/blob/master/src/main/kotlin/com/github/iyboklee/server/Main.kt) 입니다.

Main 메소드 실행 시 -config=<application.conf> 실행인자를 통해 설정파일 [application.conf](https://github.com/iyboklee/ktor-api/blob/master/conf/local/application.conf)을 넘겨줘야 합니다.

아래와 같이 maven을 통해 실행이 가능합니다.

* maven으로 빌드 및 실행
    - mvn clean compile exec:java@run
    
## REST API

총 12개의 엔드포인트를 제공합니다. [com.github.iyboklee.serving.Rest](https://github.com/iyboklee/ktor-api/blob/master/src/main/kotlin/com/github/iyboklee/serving/Rest.kt)에서 확인할 수 있습니다.

## GraphQL

GraphQL의 오직 1개의 엔드포인트를 지닙니다. [com.github.iyboklee.serving.GraphQL](https://github.com/iyboklee/ktor-api/blob/master/src/main/kotlin/com/github/iyboklee/serving/GraphQL.kt)에서 확인할 수 있습니다.

GraphQL 서비스에 편리하게 쿼리를 실행해보기 위해 [graphiql.html](http://localhost:8080/graphiql.html) 페이지를 제공합니다.

* 기본설정에 의한 엔드포인트는 http://localhost:8080/graphiql.html 입니다.