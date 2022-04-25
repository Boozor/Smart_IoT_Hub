package ece448.iot_hub;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import ece448.grading.GradeP5;
import ece448.grading.GradeP3.MqttController;
import ece448.iot_sim.Main;
import ece448.iot_sim.SimConfig;

@SpringBootApplication
public class App {
	@Autowired
	public App(Environment env) throws Exception {
	}
	@Bean
	public Buzo buzo() {
		File dataDir = new File("./data");
		if (!dataDir.exists()){
			dataDir.mkdirs();
		}
		Buzo buzo = new Buzo("./data/buzo.db");
		buzo.createGroup("x", Arrays.asList("a", "c", "e", "g"));
		buzo.createGroup("y", Arrays.asList("b", "d", "f"));
		buzo.createGroup("z", Arrays.asList("a", "d"));
		return buzo;
	}
		
    @Bean
    public MqttController mqttController(Environment env) {
        MqttController mqtt = null;
        try {
            mqtt = new MqttController(broker, clientID, topicPrefix);
            mqtt.start();
        } catch (Exception e) {
            logger.error("MqttController E:", e);
        }
        return mqtt;
	}
	

	private static final String broker = "tcp://127.0.0.1";
	private static final String topicPrefix = "iot_ece448";
	private static final String clientID = "iot_sim";
	private static final List<String> plugNames = Arrays.asList("a", "b", "c","d", "e", "f", "g");
	public static void main(String[] args) throws Exception {
		SimConfig config = new SimConfig(8081, plugNames, broker, "testee/iot_sim", topicPrefix);
		HubConfig hubConfig = new HubConfig(8088, broker, "iot_hub", topicPrefix);

		new ece448.iot_sim.Main(config);
		new ece448.iot_hub.Main(hubConfig, new String[0]);
		// GradeP5.postGroup("x", Arrays.asList("a", "c", "e", "g"));
		// GradeP5.postGroup("y", Arrays.asList("b", "d", "f"));
		// GradeP5.postGroup("z", Arrays.asList("a", "d"));
	}
	private static final Logger logger = LoggerFactory.getLogger(App.class);
}