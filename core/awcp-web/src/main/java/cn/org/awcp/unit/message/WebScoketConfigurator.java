package cn.org.awcp.unit.message;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

import org.springframework.web.context.request.RequestAttributes;

public class WebScoketConfigurator extends ServerEndpointConfig.Configurator {
	@Override
	public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
		config.getUserProperties().put(RequestAttributes.REFERENCE_REQUEST, request);
	}
}
