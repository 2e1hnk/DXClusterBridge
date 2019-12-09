package cafe.deadbeef.dx_cluster_mqtt_bridge;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	
	static Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	TelnetDXClusterMessagePublisher telnetDXClusterMessagePublisher;
	
	@Autowired
	DXClusterClient dxClusterClient;

    public static void main(String[] args) {
    	ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    	SpringApplication.run(Application.class, args);
    }
    
    @PostConstruct
    private void start() {
    	
    	//dxClusterClient.startClient();
    	
    	// Send dummy spot
        //Spot dummySpot = new Spot("N0CALL", 144000.000, "N0CALL", "Dummy Spot - ignore", 1234);
        //logger.info("Sending dummy spot: " + dummySpot);
        
        //telnetDXClusterMessagePublisher.publishEvent(dummySpot);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
