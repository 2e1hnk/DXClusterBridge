package cafe.deadbeef.dx_cluster_mqtt_bridge;

import org.springframework.context.ApplicationEvent;

public class TelnetDXClusterMessageEvent extends ApplicationEvent {
	
	private Spot spot;

	public TelnetDXClusterMessageEvent(Object source, Spot spot) {
		super(source);
		this.spot = spot;
	}
	
	public Spot getSpot() {
		return this.spot;
	}
	

}
