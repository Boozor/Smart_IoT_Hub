package ece448.iot_hub;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import ece448.grading.GradeP5;
import ece448.iot_sim.SimConfig;

@SpringBootApplication
public class App {
	@Autowired
	public App(Environment env) throws Exception {
	}
	@Bean
	public PlugModel plugModel(Environment env) throws Exception {
		String broker = env.getProperty("mqtt.broker");
		String clientID = env.getProperty("mqtt.clientId");
		String topicPrefix = env.getProperty("mqtt.topicPrefix");
		PlugModel plugModel = new PlugModel(broker, clientID, topicPrefix);
		logger.info("MqttClient {} connected: {}", clientID, broker);
		return plugModel;
	}
	private static final String broker = "tcp://127.0.0.1";
	private static final String topicPrefix = System.currentTimeMillis()+"/grade_p4/iot_ece448";
	private static final List<String> plugNames = Arrays.asList("a", "b", "c");
	private static final List<String> plugNamesEx = Arrays.asList("d", "e", "f", "g");
	public static void main(String[] args) throws Exception{
		SimConfig config = new SimConfig(8080, plugNames, broker, "testee/iot_sim", topicPrefix);
		SimConfig configEx = new SimConfig(8081, plugNamesEx, broker, "ex_testee/iot_sim", topicPrefix);
		HubConfig hubConfig = new HubConfig(8088, broker, "testee/iot_hub",   topicPrefix);

		ece448.iot_sim.Main m = new ece448.iot_sim.Main(config);
		ece448.iot_sim.Main mex = new ece448.iot_sim.Main(configEx);
		ece448.iot_hub.Main hub = new ece448.iot_hub.Main(hubConfig, new String[0]);
		GradeP5.postGroup("x", Arrays.asList("a", "c", "e", "g"));
		GradeP5.postGroup("y", Arrays.asList("b", "d", "f"));
		GradeP5.postGroup("z", Arrays.asList("a", "d"));
	} 

	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
}