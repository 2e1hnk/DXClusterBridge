package cafe.deadbeef.dx_cluster_mqtt_bridge;

import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MqttConnection {

	@Value("${mqtt.host}")
	private String host = "127.0.0.1";
	
	@Value("${mqtt.port}")
	private String port = "1883";
	
	String publisherId = UUID.randomUUID().toString();
	
	IMqttClient publisher;

	@Async
	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) throws MqttException {
		publisher = new MqttClient(String.format("tcp://%s:%s", host, port), publisherId);
		
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		publisher.connect(options);
	}
	
	public void publish(MqttMessage msg) throws MqttPersistenceException, MqttException {
		publisher.publish("spot-queue", msg);
	}
	
	public boolean isConnected() {
		return publisher.isConnected();
	}
}
