package cafe.deadbeef.dx_cluster_mqtt_bridge;

import java.util.UUID;

import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MqttConnection {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${mqtt.host}")
	private String host = "127.0.0.1";
	
	@Value("${mqtt.port}")
	private String port = "1883";
	
	@Value("${mqtt.username}")
	private String username = "admin";
	
	@Value("${mqtt.password}")
	private String password = "admin";
	
	String publisherId = UUID.randomUUID().toString();
	
	IMqttClient publisher;

	@Async
	public void connect() {
		logger.info(String.format("MQTT Connnecting to %s:%s", host, port));
		try {
			publisher = new MqttClient(String.format("tcp://%s:%s", host, port), publisherId, new MemoryPersistence());
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			options.setUserName(username);
			options.setPassword(password.toCharArray());
			publisher.connect(options);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@PreDestroy
	public void disconnect() {
		if ( publisher.isConnected() ) {
			try {
				logger.info("Disconnecting MQTT");
				publisher.disconnect();
			} catch (MqttException e) {
				logger.error("Failed to disconnect MQTT", e);
			}
		} else {
			logger.info("Application is shutting down but was not connected to MQTT");
		}
	}
	
	public void publish(MqttMessage msg) throws MqttPersistenceException, MqttException {
		publisher.publish("spot-queue", msg);
	}
	
	public boolean isConnected() {
		if ( publisher != null ) {
			return publisher.isConnected();
		}
		return false;
	}
}
