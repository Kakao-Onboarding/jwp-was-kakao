# Step1

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

## 기능 구현 목록
- HTTP request의 Start line 파싱
  - Method 파싱
  - URL 파싱
  - 쿼리 파라미터 파싱
    - 파싱 결과를 Map<String, String> 형태로 반환
  - HTTP 버전 파싱
- HTTP request의 메시지 바디 파싱
  - 폼 파라미터 파싱
    - 파싱 결과를 Map<String, String> 형태로 반환
- HTTP 응답 메시지 만들기
  - Status line 만들기
    - HTTP 버전
    - 상태 코드 + 메시지
  - 헤더 만들기
    - Content-Type
    - Content-Length
    - Location
    - 기타 등등
    - 맨 마지막 한 줄 띄우기
  - 메시지 바디 만들기
    - 스트링 받아서 메시지 바디에 담기
- Redirect 응답 만들기
  - 302 Status code 보내기
  - Location 헤더에 리다이렉트 할 url 담아서 응답하기
- 회원가입 기능
  - 쿼리 파라미터 맵을 받는다
  - 맵을 User 객체로 변환한다
  - User 객체를 Database 객체에 저장한다

## 설계
- HTTP Request Method Enum
  - GET, POST, ...
- HTTP Status enum
  - 상태 코드
  - 상태 메시지
  - 코드 숫자를 enum으로 변환해주는 메소드
- HttpRequest 도메인 객체
  - Http Request method(enum)
  - Request URL
  - HTTP 버전
  - 쿼리 파라미터(Map<String, String>)
  - HTTP 헤더(Map)
  - 메시지 바디
- HttpRequestParser
  - Http request 메시지를 파싱해서 HttpRequest 객체를 반환한다
  - Start line 파싱
    - Method
    - URL & 쿼리 파라미터
      - ? 로 스플릿해서 URL과 쿼리 파라미터를 구분한다
      - & 로 각각의 쿼리 파라미터를 구분한다
      - URL은 스트링, 쿼리 파라미터는 Map<String, String> 으로 갖고 있는다
    - HTTP 버전
  - 헤더 파싱
    - Map<String,String> 으로 파싱
  - 메시지 바디 파싱(읽어서 바이트 배열 형태로 가지고 있는다)
- HttpResponse
  - HttpStatus enum
  - String HttpVersion
  - Map<String, String> Headers
  - String body
- HttpResponseBuilder
  - ResponseEntity 구현체 참고