package kr.co.abandog.webSocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler{
	
	private static Map<String, WebSocketSession> users = new HashMap<String, WebSocketSession>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info(session.getId() + " 연결 됨");
		users.put(session.getId(), session);
		log.info("1:" + users.values());
		
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info(session.getId() + " 연결 종료됨");
		users.remove(session.getId());

	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		log.info(session.getId() + "로부터 메시지 수신: " + message.getPayload());
		TextMessage msg = new TextMessage(message.getPayload());

		//접속자한테 메세지 다 전달
		for (WebSocketSession s : users.values()) {
			s.sendMessage(msg);
			log.info(s.getId() + "에 메시지 발송: " + message.getPayload());
		}

	}
	
	@Override
	public void handleTransportError(
		WebSocketSession session, Throwable exception) throws Exception {
		log.info(session.getId() + " 익셉션 발생: " + exception.getMessage());
	}

}
