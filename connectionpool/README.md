# DB ConnectionPool 적용하기

## 0단계 - DataSource 다루기

### JDBC 드라이버를 사용하여 데이터베이스에 연결하는 방법
- JDBC(Java Database Connectivity)란, 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API
- 를 사용하면 데이터 소스와 연결하고 쿼리와 업데이트 문을 보내고 결과를 처리 가능. 자
- Java 애플리케이션이 JDBC 클래스와 인터페이스를 호출하여 SQL 문을 제출하고 결과를 검색
- JDBC API는 JDBC 드라이버를 통해 구현  
- JDBC 모델 구성 요소 ⬇️

  <img width="184" alt="image" src="https://user-images.githubusercontent.com/28749734/193180301-69473777-747b-4338-bfc3-071c88c8b7db.png">


- JDBC 드라이버: JDBC 호출을 처리하고, 결과를 Java 애플리케이션으로 리턴하기 위한 JDBC 인터페이스 구현체(클래스)들의 집합
- JDBC API의 주요 객체
  - DataSource 객체: connection(연결) 설정 시 사용. Driver Manager, DataSource 객체(더 선호됨)를 통해 연결하는 방법이 있다.
  - Connection 객체: db connection을 제어. 메서드를 통해 애플리케이션 동작 방식 변경 가능하고 statement를 생성
  - Statement, PreparedStatement, CallableStatement 객체: SQL 문을 실행
  - ResultSet 객체: SQL 쿼리가 명령문 개체에 의해 실행될 때 애플리케이션에 반환. 쿼리 결과 반환을 위한 메서드 제공

## Driver Manager vs. DataSource
- DriverManager
  - JDBC 드라이버를 관리하는 가장 기본적인 방법 
  - 커넥션 풀, 분산 트랜잭션을 지원하지 않아서 잘 사용하지 않는다.
  - JDBC 4.0 이전에는 Class.forName 메서드를 사용하여 JDBC 드라이버를 직접 등록해야 했다.
  - JDBC 4.0 부터 DriverManager가 적절한 JDBC 드라이버를 찾는다.
- DataSource
  - 데이터베이스, 파일 같은 물리적 데이터 소스에 연결할 때 사용하는 인터페이스.  
  - 구현체는 각 vendor에서 제공한다.
- DirverManager가 아닌 DataSource를 사용하는 이유
  - 애플리케이션 코드를 직접 수정하지 않고 properties로 디비 연결을 변경할 수 있다.
  - 커넥션 풀링(Connection pooling) 또는 분산 트랜잭션은 DataSource를 통해서 사용 가능하다.

## 1단계 - 커넥션 풀링
- 커넥션 풀링이란? `JdbcConnectionPool`
- 왜 스프링 부트에서 HikariCP를 사용할까
- HikariCP에 어떤 설정을 하면 좋을까

## 2단계 - HikariCP 설정하기
- 스프링 부트로 HikariCP를 설정하는 방법
- 커넥션 풀 동작 테스트

---
**Ref.**
- https://www.progress.com/faqs/datadirect-jdbc-faqs/how-does-jdbc-work
- https://docs.oracle.com/javase/8/docs/api/java/sql/DriverManager.html

