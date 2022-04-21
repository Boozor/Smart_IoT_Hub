package ece448.iot_hub;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ece448.grading.GradeP3.MqttController;
// @Component
public class PlugModel {
    

    private Map<String, Map<String, Object>> plugs = new ConcurrentHashMap<>();
    // @Autowired
    private MqttController mqtt;
    // public PlugModel(){}

    public PlugModel(String broker, String clientID, String topicPrefix) throws Exception {

        // String broker = env.getProperty("mqtt.broker");

        // String clientID = env.getProperty("mqtt.clientId");

        // String topicPrefix = env.getProperty("mqtt.topicPrefix");

        this.mqtt = new MqttController(broker, clientID, topicPrefix);

        this.mqtt.start();

    }

    synchronized public List<String> getPlugs() {

        return new ArrayList<>(plugs.keySet());

    }

    synchronized public void setPlugs(String plug, List<String> members) {

        Map<String, Object> plugProperties = new HashMap<>();

        plugProperties.put("name", members);

        plugs.put(plug, plugProperties);

    }

    

    synchronized public void publishAction(String plug, String action) {

        mqtt.publishAction(plug, action);

    }

    synchronized public String getState(String plugName) {

        Map<String, Object> plugProperties = new HashMap<>();

        plugProperties.put("state", mqtt.getState(plugName));

        plugs.put(plugName, plugProperties);

        return mqtt.getState(plugName);

    }
    


    synchronized public Map<String, Object> getPlug(String plugName){
        logger.debug("---plug:{}",plugs.get(plugName));
        Map<String, Object> ret = new HashMap<>();

        ret.put("name", plugName);

        ret.put("state", getState(plugName));
        logger.debug("---plug:{}",ret);
        return ret;
    }

    private static final Logger logger = LoggerFactory.getLogger(GroupsResource.class);

}