package com.railly_linker.springboot_mvc_project_template.configurations

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class SwaggerConfig {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // (버전 정보)
    private val documentVersion: String = "1.0.0"

    // (문서 제목)
    private val documentTitle: String = "SpringBoot Mvc Project Template APIs"

    // (문서 설명)
    private val documentDescription: String = """
      **[읽어주세요]**
      
      **(공지)**
        - 기본 발생하는 HTTP Status Code 는 https://en.wikipedia.org/wiki/List_of_HTTP_status_codes 를 참고하세요.
          
      **(일반 규칙)**
        - Swagger 문서의 대분류는 컨트롤러라고 합니다.
          
            컨트롤러는 일련번호가 매겨져 있고, C1, C2... 이런 식으로 표시됩니다.
          
            또한 컨트롤러 안의 소분류인 API 항목 역시 일련번호가 매겨져 있으며, N1, N2... 이런 식으로 표시됩니다.
          
            API 관련 요청을 할때는, "/tk/request-test/get-request" 이렇게 주소로 표현하거나 혹은, 
          
            "C2-N4 API", 혹은 "2-4 API" 와 같은 일련번호로 표현하여도 좋습니다.
        - 리스트를 반환하는 Rest API 의 경우, 기획상 반환 수량의 한계가 정해져있다면 통째로 반환해주고,
      
            그외에 수량이 무한히 추가될 가능성이 있다면(논리적 가능성을 우선) 페이징을 하여 반환 하도록 설계할 것입니다.
      
            위 규칙을 준수하되, 서버 개발자 판단 및 논의에 따라 예외가 적용될 수 있습니다.
        - Input Json String 표기법
      
            multipart/form-data 형식에서 Object List 타입의 데이터를 수집할 때나 RequestBody 를 사용 불가능한 Get Method 에서 복잡한 input 을 받을 때에는,
      
            request 에 json 형식의 request body 를 그대로 받을수 없기에 json 형식의 String 을 통해 정보를 전달해야합니다.
      
            사용자가 입력한 json 형식 string 을 서버에서 해석하여 데이터를 분리할 것이기에, 
      
            json String 을 입력할 때에 지켜야할 데이터 형식을 API 의 jsonString 파라미터 설명에 적을 것입니다.
      
            표기법은 kotlin 의 data class 를 사용할 것이기에 클라이언트 개발자 분들의 양해 부탁드립니다.
      
            표기 예시로,
      
                data class SampleInputVo(
      
                    @Schema(description = "샘플 텍스트", example = "sampleTextExample")
      
                    @JsonProperty("sample-text")
      
                    val sampleText: String,
      
                    @Schema(description = "샘플 숫자", example = "1")
      
                    @JsonProperty("sample-int")
      
                    val sampleInt: Int?,
      
                    @Schema(description = "샘플 불린", example = "1")
      
                    @JsonProperty("sampleBool")
      
                    val sampleBoolean: Boolean
      
                )
      
            위와 같습니다.
      
            해석법은 아래와 같습니다.
      
            data class 는 클래스 선언을 뜻하며, SampleInputVo 는 서버 내에서 사용하는 클래스명입니다. 
      
            @Schema 태그 부터 다음 @Schema 태그 앞까지는 한 변수와 그에 대한 설명의 세트를 의미합니다.
      
            @Schema 태그의 속성인 description 은 변수의 설명, example 은 변수의 입력값 예시(타입 상관 없이 "" 로 묶임) 를 뜻하며,
      
            @JsonProperty 태그는 입력값 변수명으로, 아래의 val sampleText 와 같은 코틀린 코드 내부 변수명 대신 이것을 API 요청 입력값으로 사용하면 됩니다.
      
            val sampleText: String 부분의 경우는 val 은 코드 내 변수 선언 키워드이고, sampleText 는 코틀린 코드 내부에서 사용하는 변수명이며, 
      
            : 뒤의 String, Int, List, Boolean 과 같은 표기는 변수타입, 변수타입 뒤의 ? 가 붙어있으면 nullable, 붙어있지 않다면 not nullable 이라는 뜻입니다.
      
            고로 위의 예시의 경우 json String 은
      
                {"sample-text": "샘플 텍스트",  "sample-int": 1, "sampleBool" : true}
      
            혹은
      
                {"sample-text": "샘플 텍스트",  "sample-int": null, "sampleBool" : false}
      
            위와 같은 json 형식의 string 으로 입력하면 됩니다.
      
            클라이언트에서 사용하는 각 프로그래밍 언어별 지원해주는 JsonString <-> Object 라이브러리를 사용하면 편하게 입력 처리가 가능합니다.
      
      **(인증/인가 관련 규칙)**
        - API path tk/** 는 Token 인증/인가 방식을 사용하는 API 를 뜻하며, 나머지는 세션-쿠키 인증/인가 방식을 사용합니다.
        - API 항목의 제목 문장의 마지막에 붙는 "<>" 는 해당 API 사용을 위한 권한을 의미합니다.
      
            예를들어 권한이 필요 없으면 표시 하지 않고, 로그인이 필요한 경우 <>, 
          
            특정 권한이 필요한 경우 <'ADMIN' or ('DEVELOPER' and 'MANAGER')> 와 같은 형식으로 표시됩니다.
        - <>? 이 붙는다면 해당 API 는 로그인이 필수는 아니지만 로그인 시와 비로그인 시의 결과값이 달라진다는 것을 의미합니다.
        
            <>? 이 붙은 API 를 호출할 때, 
          
            클라이언트에서는, 현재 로그인 되어있다면 인증/인가 토큰을 보내주면 되고, 로그인 되어있지 않다면 토큰을 보내주지 않으면 됩니다.
        - tk/** 에서 로그인이 필요한 API 요청시엔 네트워크 요청 Header 에 "Authorization" 이란 키로 
      
            로그인 API 에서 반환된 토큰 타입과 토큰 String 을 붙여서 보내주면 됩니다.
      
            예를들어 로그인시 토큰 타입이 "Bearer", 토큰 String 이 "abcd1234" 로 반환되었다면,
      
            {"Header" : {"Authorization" : "Bearer abcd1234"}}
      
            위와 같은 형식으로, 토큰 타입 뒤에 한칸을 띄우고 토큰 String 을 합쳐서 헤더에 Authorization 이란 이름으로 보내면 됩니다.
        - <> 가 붙지 않은(로그인이 필요치 않은) API 에도 Authorization Request Header 를 보내줘도 무방합니다.
        
            다만, 서버는 Authorization 이 요청으로 받는 순간, 
            
            API 의 기능에 로그인 여부가 필요하건 하지 않건 무조건 받은 Authorization 의 토큰을 검증하게 되며, 
            
            검증 후 액세스 토큰 만료와 같은 결격 사유가 발견되면 정상 응답이 아닌 에러가 반환됩니다.
            
            즉, 로그인이 필요 없는 API 에 Authorization 을 보낸다면 로그인이 필요한 API 에 Authorization 을 보냈을 때와 동일하게 처리해야합니다.
        - tk/** 에서 사용하는 RefreshToken 은 AccessToken 발급(SignIn, Reissue api 사용)시마다 재발행됩니다.
      
            AccessToken 보다 더 길게 설정된 RefreshToken 의 만료시간 동안 토큰 재발급을 전혀 하지 않아 RefreshToken 마저 만료된 경우, 
      
            만료된 RefreshToken 으로 reissue api 를 사용시엔 만료 에러가 떨어지는데, 이때는 클라이언트 측에서 멤버에게 재로그인을 요청해야합니다.
        - JWT 사용 관련 클라이언트 측 처리 알고리즘은 아래를 참고하시면 됩니다.
        
            1. 클라이언트 비휘발성 저장소 안에,
                
                    class SignInInfoVo {
                      String memberUid; // 멤버 고유값
                      String nickName; // 닉네임
                      String tokenType; // 발급받은 토큰 타입(ex : "Bearer")
                      String accessToken; // 액세스 토큰 (ex : "aaaaaaaaaa111122223333")
                      String accessTokenExpireWhen; // 액세스 토큰 만료일시 (ex : "2023-01-02 11:11:11.111")
                      String refreshToken; // 리프레시 토큰 (ex : "rrrrrrrrrr111122223333")
                      String refreshTokenExpireWhen; // 리프레시 토큰 만료일시 (ex : "2023-01-02 11:11:11.111")
                    }
                
                위와 같은 형태로 인증 / 인가 정보와 회원 정보를 저장하도록 준비합니다.
                
                위 저장소가 null 이라면 현재 비회원 상태, not null 이라면 현재 회원 로그인 상태입니다.
                
                즉, 위 저장소를 채우는 것이 로그인, 위 저장소를 비우는 것이 로그아웃입니다.
                
            2. 서버가 공개한 로그인 API 로 로그인을 했다면, 서버가 반환하는 정보를 위의 저장소에 저장합니다.
            
            3. 로그인이 필요한 API 에 요청을 보낼시, 
            
                먼저 로그인 정보 저장소에 정보가 저장되어 있는지(= 로그인이 되어있는지) 확인합니다.
                
                만약 비회원 상태라면 해당 API 를 사용할 수 없으니 유저에게 로그인을 요청하고, 
                
                회원 상태라면 Request Header 의 Authorization 키 안에 "{totenType} {accessToken}" 을 실어 보냅니다.
            
            4. Authorization 을 보낸 API 에서 응답이 왔을 때 다음과 같은 신호가 올 수 있습니다. 
            
                1) 액세스 토큰 만료 신호
                
                    말 그대로 현재 전송한 액세스 토큰이 만료된 경우입니다.
                
                    이때는 저장 되어 있는 리프레시 토큰으로 액세스 토큰 재발급을 요청합니다.
                    
                    이에대한 응답으로 리프레시 토큰이 만료되었다는 신호가 온다면 로그아웃 처리 후 유저에게 재로그인을 요청합니다.
                    
                    재발급이 완료되었다면 로그인 정보 저장소의 정보들을 갱신하고, 
                    
                    최신 로그인 정보로 다시 요청을 보냅니다. 
                
                2) 회원 정보가 없다는 신호
                
                    이는 액세스 토큰을 발급받은 시점에는 회원가입이 되어있다가 모종의 이유로 회원 정보가 삭제된 경우입니다.
                    
                    로그아웃 처리 후 유저에게 에러 정보를 전달하고 다른 계정으로 재로그인을 요청하면 됩니다.
                    
                3) 올바르지 않은 액세스 토큰 신호
                    
                    전송한 액세스 토큰의 형태가 올바르지 않은 경우입니다.
                    
                    사전에 테스트를 완료했다면 일어날 일이 없어보이지만, 
                    
                    서버에서 정보 유출 등의 이유로 암호화 키를 변경하거나, 토큰 형태를 변경하거나, 유효기간을 변경하는 등의 경우에 일어날 수 있습니다.
                    
                    로그아웃 처리 후 유저에게 에러 정보를 전달하고 재로그인을 요청하면 됩니다.
                    
                4) 권한이 충족되지 않은 신호 (403)
                
                    로그인이 되었더라도 해당 계정이 API 가 요구하는 권한을 충족하지 못하는 경우에 나타납니다.
                    
                    권한이 없다는 정보를 유저에게 전달하면 됩니다.
                    
                위와 같이 각각의 상황에 대해 처리를 해주세요.
                
            5. 인증 / 인가 관련 에러가 발생하지 않았다면 정상 처리를 진행하면 됩니다.
        - Swagger 토큰 인증이 필요한 API(tk/**) 테스트를 하기 위해선,
      
            아래의 Authorize 버튼 혹은 각 API 의 자물쇠 버튼을 누르고 value 에 해당 권한의 토큰을 넣어서 로그인 한 후 요청을 보내면 됩니다.
      
            이땐 Bearer 과 같은 토큰 타입을 붙이지 않고 순수 토큰 String 만을 입력합니다. (ex : "abcd1234")
      
      **(Rest API Response Header 의 API 결과 표시 규칙)**
        - 본 서버에서 내려주는 Http Status Code 는 무의미합니다. 
        
            아래 설명을 필독하세요.
        - 본 서버에서 제공하는 모든 Rest API 의 Response Header 에는 api-result-code 라는 키가 포함됩니다.
            
            예를들어 데이터 수정 API 를 요청한다고 했을 때, 
            
            사용자가 의도한 것은 데이터를 수정하고, 수정된 데이터로 인해 생성된 문서 파일의 경로를 String 으로 받는 것이라고 가정하겠습니다.
            
            하지만 이러한 의도와는 다른 결과가 발생할 수 있는 상황을 몇가지 가정하자면,
            
            1. 서버에서 정해놓은 데이터 저장 권한이 요청자에게 없음
            
            2. 현재 수정 대상이 지워져 있음
            
            3. 현재 수정 대상이 수정 가능 상태가 아님
            
            위와 같은 원인이 존재할 수 있습니다.
            
            네트워크 요청을 처리하려 했을 때, 서버에서 위와 같은 상황이 캐치되었다면
            
            원하는 정보인 생성 문서 파일의 경로는 있을 수 없기에 결과값을 null 로 반환을 할 수밖에 없는데,
            
            클라이언트 측의 입장에선 무슨 이유로 결과값을 받아올 수 없는지를 알아야 하기에 서버에선 각 원인을 "api-result-code" 에 담아 주는 것입니다.
            
            위와 같은 에러가 아닌 성공시에도 무조건 api-result-code 를 "ok" 로 보내줄 것이며,
            
            각 API 사용별 반환될 수 있는 "api-result-code" 는 Swagger 문서에 기록할 것입니다.
        - 클라이언트에서의 api-result-code 처리 방식의 알고리즘은 아래와 같이 처리하면 됩니다.
        
            1. 네트워크 요청을 보내기
            
            2. 사용중인 네트워크 라이브러리가 반환하는 에러 확인(타임아웃 에러, 서버접근 불가 에러 등)
            
            3. 네트워크 라이브러리 에러가 없다면 Response Header 안에 api-result-code 가 있는지 확인
            
            4. api-result-code 가 있다면, 코드 종류에 따라 스웨거 문서를 확인하여 적절한 처리를 해주기 (에러가 아닌 성공시에도 무조건 반환됩니다.)
            
            5. api-result-code 가 없다면, 서버 개발자에게 문의하기
      
      **(전역 api-result-code)**
        - 전역 결과 코드는, 말 그대로 모든 API 에서 공통적으로 발생할 가능성이 있는 코드입니다.
        
        - 모든 상황
        
            ok : API 정상 처리 완료
        
        - Request Header 에 Authorization 입력시
        
            a : 만료된 AccessToken. (reissue, sign-out API 는 제외)
            
            b : 토큰 발급시엔 존재했지만, 이제는 존재하지 않는 멤버
            
            c : 올바르지 않은 AccessToken
            
            d : 로그인 계정에 API 호출 권한이 없습니다. (Http Status Code 403)
    """.trimIndent()


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(
                Components().addSecuritySchemes(
                    "JWT",
                    SecurityScheme().apply {
                        this.type = SecurityScheme.Type.HTTP
                        this.scheme = "bearer"
                        this.bearerFormat = "JWT"
                        this.`in` = SecurityScheme.In.HEADER
                        this.name = HttpHeaders.AUTHORIZATION
                    })
            )
            .addSecurityItem(
                SecurityRequirement().apply {
                    this.addList("JWT")
                }
            )
            .info(Info().apply {
                this.title(documentTitle)
                this.version(documentVersion)
                this.description(documentDescription)
            })
    }
}