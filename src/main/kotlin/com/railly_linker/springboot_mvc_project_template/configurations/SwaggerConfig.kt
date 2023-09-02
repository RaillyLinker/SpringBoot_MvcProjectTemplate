package com.railly_linker.springboot_mvc_project_template.configurations

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    // <멤버 변수 공간>
    private val loggerMbr: Logger = LoggerFactory.getLogger(this::class.java)

    // (버전 정보)
    private val versionMbr: String = "1.0.0"

    // (문서 제목)
    private val documentTitleMbr: String = "SpringBoot Mvc Project Template APIs"

    // (문서 설명)
    private val documentDescriptionMbr: String = """
      **[읽어주세요]**
      
      **(공지)**
        - 기본 발생하는 HTTP Status Code 는 https://en.wikipedia.org/wiki/List_of_HTTP_status_codes 를 참고하세요.
        
        - 적극적인 에러 제보 부탁드립니다.
          
          에러가 발생한 API 주소, 발생 시간, 전달 파라미터, 응답 로그 등을 남겨주시면 보다 빠르게 처리가 가능합니다.
          
      **(일반 규칙)**
        - API path tk/** 는 Token 인증/인가 방식을 사용하는 API 를 뜻하며, 나머지는 세션-쿠키 인증/인가 방식을 사용합니다.
        - API 항목의 제목 문장의 마지막에 붙는 "<>" 는 해당 API 사용을 위한 권한을 의미합니다.
      
          예를들어 권한이 필요 없으면 표시 하지 않고, 로그인이 필요한 경우 <>, 
          
          특정 권한이 필요한 경우 <'ADMIN' or ('DEVELOPER' and 'MANAGER')> 와 같은 형식으로 표시됩니다.
        - <>? 이 붙는다면 해당 API 는 로그인이 필수는 아니지만 로그인 시와 비로그인 시의 결과값이 달라진다는 것을 의미합니다.
        
          <>? 이 붙은 API 를 호출할 때, 
          
          클라이언트에서는, 현재 로그인 되어있다면 인증/인가 토큰을 보내주면 되고, 로그인 되어있지 않다면 토큰을 보내주지 않으면 됩니다.
        - 리스트를 반환하는 Rest API 의 경우, 기획상 반환 수량의 한계가 정해져있다면 통째로 반환해주고,
      
          그외에 수량이 무한히 추가될 가능성이 있다면(논리적 가능성을 우선) 페이징을 하여 반환 하도록 설계할 것입니다.
      
          위 규칙을 준수하되, 서버 개발자 판단 및 논의에 따라 예외가 적용될 수 있습니다.
        - 클라이언트 분들은 요청사항이 있으시다면 IP 번호와 포트번호를 제외한 나머지 URL 을 사용하여 요청을 명확히 해주시길 부탁드립니다.
      
          예를들어, 127.0.0.1:8080/test/sample 이라는 API 의 반환값에 문제가 있어서 수정을 요구하신다면,
          
          "/test/sample 의 somethingCode 를 숫자가 아닌 한글 단어로 반환해주세요."
          
          "/test/sample 을 /test/something API 를 사용한 직후 호출하면 500 에러가 뜹니다. 금일 15 : 43 에 요청하였으며, 
          
          제가 요청한 파라미터는 이러하며, 응답은 이러합니다."
          
          이런식으로 요청해주시길 바랍니다.
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
    """.trimIndent()


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(Info().apply {
                this.title(documentTitleMbr)
                this.version(versionMbr)
                this.description(documentDescriptionMbr)
            })
    }
}