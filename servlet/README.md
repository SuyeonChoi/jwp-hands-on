# 서블릿 구현하기

### 1단계 - 서블릿 학습 테스트

- `SharedCounterServlet`, `LocalCounterServlet` 클래스 차이
  -  `SharedCounterServlet`에는 인스턴스 변수 `sharedCounter`가 존재한다. 인스턴스 변수는 스레드끼리 공유되므로 `service`가 호출된 만큼 개수가 증가하여 요청마다 응답이 달라진다. `LocalCounterServlet`는 지역변수인 `localCounter`를 증가시키므로 응답이 항상 같다.

### 2단계 - 필터 학습 테스트

- `doFilter` 메서드의 실행 시점
  - `init()` 이후, `service()` 이전 
  ``` shell
  Sep 17, 2022 7:38:29 PM org.apache.coyote.AbstractProtocol init
    INFO: Initializing ProtocolHandler ["http-nio-8080"]
    Sep 17, 2022 7:38:29 PM org.apache.catalina.core.StandardService startInternal
    INFO: Starting service [Tomcat]
    Sep 17, 2022 7:38:29 PM org.apache.catalina.core.StandardEngine startInternal
    INFO: Starting Servlet engine: [Apache Tomcat/10.1.0-M16]
    Sep 17, 2022 7:38:29 PM org.apache.catalina.startup.ContextConfig getDefaultWebXmlFragment
    INFO: No global web.xml found
    Sep 17, 2022 7:38:30 PM org.apache.jasper.servlet.TldScanner scanJars
    INFO: At least one JAR was scanned for TLDs yet contained no TLDs. Enable debug logging for this logger for a complete list of JARs that were scanned but no TLDs were found in them. Skipping unneeded JARs during scanning can improve startup time and JSP compilation time.
    Sep 17, 2022 7:38:30 PM org.apache.coyote.AbstractProtocol start
    INFO: Starting ProtocolHandler ["http-nio-8080"]
    Sep 17, 2022 7:38:31 PM org.apache.catalina.core.ApplicationContext log
    INFO: init() 호출
    Sep 17, 2022 7:38:31 PM org.apache.catalina.core.ApplicationContext log
    INFO: doFilter() 호출
    Sep 17, 2022 7:38:31 PM org.apache.catalina.core.ApplicationContext log
    INFO: service() 호출
    Sep 17, 2022 7:38:31 PM org.apache.coyote.AbstractProtocol pause
    INFO: Pausing ProtocolHandler ["http-nio-8080"]
    Sep 17, 2022 7:38:31 PM org.apache.catalina.core.StandardService stopInternal
    INFO: Stopping service [Tomcat]
    Sep 17, 2022 7:38:31 PM org.apache.catalina.core.ApplicationContext log
    INFO: destroy() 호출
    Sep 17, 2022 7:38:31 PM org.apache.coyote.AbstractProtocol stop
    INFO: Stopping ProtocolHandler ["http-nio-8080"]
  ```
- 인코딩을 설정해야하는 이유
  - 응답할 때의 charset을 따로 지정하지 않으면 `ISO-8859-1`으로 지정된다.
  - `setCharacterEncoding`과 `setContentType` 메서드를 또는 `setLocale` 메서드를 사용해서 응답의 charset을 설정할 수 있다.
    - character encoding을 사용하기 위해서 해당 메서드들은 `getWriter` 전과 응답을 커밋하기 전에 호출되어야한다. 
