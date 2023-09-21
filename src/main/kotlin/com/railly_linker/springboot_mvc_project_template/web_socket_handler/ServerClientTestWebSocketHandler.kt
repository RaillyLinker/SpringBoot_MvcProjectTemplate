package com.railly_linker.springboot_mvc_project_template.web_socket_handler

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore

// [1대1 웹 소켓 테스팅 핸들러]
// WebSocketConfig 에서 설정에 사용됨
// 텍스트 데이터 양방향 연결
class ServerClientTestWebSocketHandler : TextWebSocketHandler() {
    // <멤버 변수 공간>
    private val classLogger: Logger = LoggerFactory.getLogger(this::class.java)

    // (현재 웹 소켓에 연결된 세션 리스트)
    private val webSocketSessionHashMap: HashMap<String, WebSocketSession> = HashMap()
    private val webSocketSessionArrayListSemaphore: Semaphore = Semaphore(1)

    // (스레드 풀)
    private val executorService: ExecutorService = Executors.newCachedThreadPool()


    // ---------------------------------------------------------------------------------------------
    // <공개 메소드 공간>
    // (Client 연결 콜백)
    @Throws(Exception::class)
    override fun afterConnectionEstablished(
        // 연결된 클라이언트 세션
        webSocketSession: WebSocketSession
    ) {
        // 세션을 리스트에 추가
        webSocketSessionArrayListSemaphore.acquire()
        // session id 가 각 세션을 구분하는 고유값
        webSocketSessionHashMap[webSocketSession.id] = webSocketSession
        webSocketSessionArrayListSemaphore.release()
    }

    // (Client 해제 콜백)
    @Throws(Exception::class)
    override fun afterConnectionClosed(
        // 연결 해제된 클라이언트 세션
        webSocketSession: WebSocketSession,
        status: CloseStatus
    ) {
        // 세션을 리스트에서 제거
        webSocketSessionArrayListSemaphore.acquire()
        webSocketSessionHashMap.remove(webSocketSession.id)
        webSocketSessionArrayListSemaphore.release()
    }

    // (메세지 수신 콜백)
    @Throws(Exception::class)
    override fun handleTextMessage(
        // 메세지를 보낸 클라이언트 세션
        webSocketSession: WebSocketSession,
        // 수신된 메세지
        message: TextMessage
    ) {
        val payload = message.payload

        // 보내온 String 메세지를 객체로 해석
        val messagePayloadVo = Gson().fromJson<MessagePayloadVo>(
            payload, // 해석하려는 json 형식의 String
            object : TypeToken<MessagePayloadVo>() {}.type // 파싱할 데이터 스키마 객체 타입
        )

        classLogger.info("messagePayloadVo : $messagePayloadVo")

        // 메세지 수신 후 몇초 후 서버 사이드에서 메세지 전송
        executorService.execute {
            // 범위 랜덤 밀리초 대기
            val randomMs = (0L..2000L).random()
            Thread.sleep(randomMs)

            // 메세지를 보낸 클라이언트에게 메세지 전송
            webSocketSession.sendMessage(
                TextMessage(
                    // 객체를 Json String 으로 해석
                    Gson().toJson(
                        MessagePayloadVo(
                            "Server",
                            "$randomMs 밀리초 대기 후 전송함"
                        )
                    )
                )
            )
        }
    }


    // ---------------------------------------------------------------------------------------------
    // <비공개 메소드 공간>


    // ---------------------------------------------------------------------------------------------
    // <중첩 클래스 공간>
    // (메세지 스키마)
    //     고도화시에는 아래 VO 에 더 많은 정보를 저장하여 이를 이용하여 기능을 구현할것.
    //     일반적으로 양방향 연결이 필요한 기능인 채팅에 관련하여,
    //     필요한 기능들에 필요한 형식을 미리 만들어서 제공해주는 프로토콜인 STOMP 를 사용할 수도 있음.
    data class MessagePayloadVo(
        val sender: String, // 송신자 (실제로는 JWT 로 보안 처리를 할 것)
        val message: String
    )

}