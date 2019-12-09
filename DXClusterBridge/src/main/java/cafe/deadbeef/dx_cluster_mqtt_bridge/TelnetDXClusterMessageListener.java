package cafe.deadbeef.dx_cluster_mqtt_bridge;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TelnetDXClusterMessageListener implements ApplicationListener<TelnetDXClusterMessageEvent> {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired MqttConnection mqttConnection;
	
    @Override
    public void onApplicationEvent(TelnetDXClusterMessageEvent event) {
        logger.info("Received spring custom event - " + event.getSpot());
        if ( !mqttConnection.isConnected() ) {
        	logger.info("Attempting to connect to MQTT...");
        	try {
				mqttConnection.connect();
				Thread.sleep(1000);
			} catch (MqttException e) {
				logger.error("Error connecting to MQTT Broker", e);
			} catch (InterruptedException e) {
				logger.error("Couldn't wait for MQTT Connection", e);
			}
        }
        if ( mqttConnection.isConnected() ) {
        	String spotJson = event.getSpot().toString();
        	MqttMessage msg = new MqttMessage(spotJson.getBytes());
        	msg.setQos(0);
        	msg.setRetained(true);
            try {
				mqttConnection.publish(msg);
			} catch (MqttException e) {
				e.printStackTrace();
			}
        } else {
        	logger.error("Can't send spot - MQTT not connected");
        }
    }
    
}