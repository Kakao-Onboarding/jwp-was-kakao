# 웹 애플리케이션 서버
## 진행 방법
* 웹 애플리케이션 서버 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

# 🚀 1단계 - HTTP 웹 서버 구현하기
## 미션 설명
- 간단한 HTTP 웹 서버를 만들어본다.
- 저장소에서 소스코드를 받아와서 메인 클래스를 실행하면 HTTP 서버가 실행된다. 웹브라우저로 로컬 서버 `http://localhost:8080`에 접속하면 `Hello world`가 보인다.


## 기능 요구사항

### 1. GET /index.html 응답하기
- http://localhost:8080/index.html에 접근할 수 있도록 구현한다. 
- RequestHandlerTest 테스트가 모두 통과하도록 구현한다. 

**HTTP Request Header 예**
```http request
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

**모든 Request Header 출력하기 힌트**
- InputStream => InputStreamReader => BufferedReader 
- 구글에서 “java inputstream bufferedreader “로 검색 후 문제 해결 
- BufferedReader.readLine() 메소드 활용해 라인별로 http header 읽는다. 
- http header 전체를 출력한다. 
- header 마지막은 while (!"".equals(line)) {} 로 확인 가능하다. 
- line이 null 값인 경우에 대한 예외 처리도 해야 한다. 그렇지 않을 경우 무한 루프에 빠진다.(if (line == null) { return;})

**Request Line에서 path 분리하기 힌트**
- Header의 첫 번째 라인에서 요청 URL(위 예의 경우 /index.html 이다.)을 추출한다. 
- String[] tokens = line.split(" "); 활용해 문자열을 분리할 수 있다. 
- 구현은 별도의 유틸 클래스를 만들고 단위 테스트를 만들어 진행할 수 있다.


**path에 해당하는 파일 읽어 응답하기 힌트**
- 요청 URL에 해당하는 파일을 src/main/resources 디렉토리에서 읽어 전달하면 된다. 
- utils.FileIoUtils의 loadFileFromClasspath() 메소드를 이용해 classpath에 있는 파일을 읽는다. 
```java
public class FileIoUtilsTest {
  private static final Logger log = LoggerFactory.getLogger(FileIoUtilsTest.class);
  
  @Test
  void loadFileFromClasspath() throws Exception {
  byte[] body = FileIoUtils.loadFileFromClasspath("./templates/index.html");
  log.debug("file : {}", new String(body));
  }
}
```

### 2. CSS 지원하기
- 인덱스 페이지에 접속하면, 현재 stylesheet 파일을 지원하지 못하고 있다. Stylesheet 파일을 지원하도록 구현하도록 한다. 
- HTTP Request Header 예

```http request
GET ./css/style.css HTTP/1.1
Host: localhost:8080
Accept: text/css,*/*;q=0.1
Connection: keep-alive
```

### 3. Query String 파싱
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입할 수 있다. 
- 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다. 
- HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다. 
- 회원가입할 때 생성한 User 객체를 DataBase.addUser() 메서드를 활용해 RAM 메모리에 저장한다.

**HTTP Request Header 예**
```http request
GET /user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

**Request Parameter 추출하기 힌트**
- Header의 첫 번째 라인에서 요청 URL을 추출한다. 
- 요청 URL에서 접근 경로와 이름=값을 추출해 User 클래스에 담는다. 
- 접근 경로(위 예에서는 /user/create)와 패러미터를 분리하는 방법은 String의 split() 메소드를 활용하면 된다. 
- ? 문자는 정규표현식의 예약어이기 때문에 split() 메소드를 사용할 "?"이 아닌 "\\?"를 사용해야 한다. 
- 구현은 가능하면 junit을 활용해 단위 테스트를 진행하면서 하면 좀 더 효과적으로 개발 가능하다.




### 4. POST 방식으로 회원가입
- http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.

**HTTP Request Header 예**
```http request
POST /user/create HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Content-Length: 59
Content-Type: application/x-www-form-urlencoded
Accept: */*

userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com
```

**Request Body의 값 추출하기 힌트**
- POST method로 데이터를 전달할 경우 전달하는 데이터는 HTTP Body에 담긴다. 
- HTTP Body는 HTTP header 이후 빈 공백을 가지는 한 줄(line) 다음부터 시작한다. 
- HTTP Body에 전달되는 데이터는 GET method의 이름=값과 같다. 
- BufferedReader에서 본문 데이터는 util.IOUtils 클래스의 readData() 메서드를 활용한다. 이 때, request header의 Content-Length가 request body의 length다. 
- 회원가입시 입력한 모든 데이터를 추출해 User 객체를 생성한다.




### 5. Redirect
- 현재는 “회원가입”을 완료 후, URL이 /user/create 로 유지되는 상태로 읽어서 전달할 파일이 없다. redirect 방식처럼 회원가입을 완료한 후 index.html로 이동해야 한다.

**redirect 힌트**
- HTTP 응답 헤더의 status code를 200이 아니라 302 code를 사용한다. 
- HTTP_302 문서 참고
