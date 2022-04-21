package ece448.iot_sim;

import static org.junit.Assert.*;
import ece448.iot_sim.PlugSim;
import ece448.iot_sim.PlugSim.Observer;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Test;

public class MqttCommandsTests {
    @Test
    public void test1() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        Observer aaa= new Observer(){
        
            @Override
            public void update(String name, String key, String value) {
                // TODO Auto-generated method stub
                
            }
        };
        plug.addObserver(aaa);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String topic="test/iot_zaynaction/"+plug.getName()+"/on";
        System.out.println(topic);
        test.handleMessage(topic, msg);
    }
    @Test
    public void test2() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String topic="test/iot_zaynaction/"+plug.getName()+"/off";
        System.out.println(topic);
        test.handleMessage(topic, msg);
    }
    @Test
    public void test3() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String topic="test/iot_zaynaction/"+plug.getName()+"/toggle";
        System.out.println(topic);
        test.handleMessage(topic, msg);
        test.handleMessage(topic, msg);
    }
    @Test
    public void test4() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String topic="test/iot_zaynaction/"+plug.getName()+"/ppp";
        System.out.println(topic);
        test.handleMessage(topic, msg);
    }
@Test
    public void test5() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String topic="test/iot_zayn/"+plug.getName()+"/play";
        System.out.println(topic);
        test.handleMessage(topic, msg);   
    }
    @Test     
    public void test6() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String subscribe=test.getTopic();
        String topic="test/iot_zayn/"+plug.getName()+"/play";
        System.out.println(topic);
        test.handleMessage(topic, msg);  
    }
    @Test
    public void test7() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String topic="test/iot_zaynaction/"+plug.getName()+"/on";
        System.out.println(topic);
        test.handleMessage(topic, msg);
        Observer aaa= new Observer(){
        
            @Override
            public void update(String name, String key, String value) {
                // TODO Auto-generated method stub
                
            }
        };
        plug.addObserver(aaa);
        topic="test/iot_zaynaction/"+plug.getName()+"/off";
        test.handleMessage(topic, msg);
        Observer bbb= new Observer(){
        
            @Override
            public void update(String name, String key, String value) {
                // TODO Auto-generated method stub
                
            }
        };
        plug.addObserver(bbb);
    }
    @Test
    public void test8() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String topic="test/iot_zaynaction/"+plug.getName()+"/on";
        System.out.println(topic);
        test.handleMessage(topic, msg);
        topic="test/iot_zaynaction/"+plug.getName()+"/toggle";
        test.handleMessage(topic, msg);
    }
    public void test9() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        PlugSim plug1 = new PlugSim("b");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        plugs.add(plug1);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String topic="test/iot_zaynaction/"+plug.getName()+"/on";
        System.out.println(topic);
        test.handleMessage(topic, msg);
    }
    public void test10() {
        String topicPrefix="test/";
        MqttMessage msg= new MqttMessage();
        PlugSim plug = new PlugSim("a");
        PlugSim plug1 = new PlugSim("b");
        ArrayList<PlugSim> plugs = new ArrayList<>();
        plugs.add(plug);
        plugs.add(plug1);
        MqttCommands test= new MqttCommands(plugs, topicPrefix);
        String topic="test/iot_zaynaction/"+plug.getName()+"/on";
        System.out.println(topic);
        test.handleMessage(topic, msg);
        topic="test/iot_zaynaction/"+plug.getName()+"/toggle";
        test.handleMessage(topic, msg);
	}
	
	@Test
	public void test_getTopic() {

		String TopicPrefix = "test";
		ArrayList<PlugSim> plugs = new ArrayList<>();
		plugs.add(new PlugSim("a"));
		plugs.add(new PlugSim("b"));

		MqttCommands mqttCmd = new MqttCommands(plugs, TopicPrefix);
		String topic = mqttCmd.getTopic();

		assertTrue("test/action/#".equals(topic));
	}

	@Test
	public void test_handleMessage_on() {

		ArrayList<PlugSim> plugs = new ArrayList<>();
		PlugSim pluga = new PlugSim("a");
		plugs.add(new PlugSim("a"));
		plugs.add(new PlugSim("b"));

		String topic = "prefix/action/a/on";
		MqttMessage msg = new MqttMessage("prueba2".getBytes());

		MqttCommands mqttCmd = new MqttCommands(plugs, "prefix");
		mqttCmd.handleMessage(topic, msg);;

		pluga.switchOn();//update

		assertTrue(pluga.isOn());
	}
	
	@Test
	public void test_handleMessage_off() {

		ArrayList<PlugSim> plugs = new ArrayList<>();
		PlugSim pluga = new PlugSim("a");
		plugs.add(new PlugSim("a"));
		plugs.add(new PlugSim("b"));

		String topic = "prefis/action/a/off";
		MqttMessage msg = new MqttMessage("prueba2".getBytes());

		MqttCommands mqttCmd = new MqttCommands(plugs, "prueba2");
		mqttCmd.handleMessage(topic, msg);;
		pluga.switchOff();//update

		assertFalse(pluga.isOn());
	}

	@Test
	public void test_handleMessage_toggle() {

		ArrayList<PlugSim> plugs = new ArrayList<>();
		PlugSim pluga = new PlugSim("a");
		plugs.add(new PlugSim("a"));
		plugs.add(new PlugSim("b"));

		String topic = "prefis/action/a/toggle";
		MqttMessage msg = new MqttMessage("prueba2".getBytes());

		MqttCommands mqttCmd = new MqttCommands(plugs, "prueba2");
		mqttCmd.handleMessage(topic, msg);;

		pluga.toggle();//update

		assertTrue(pluga.isOn());
	}

	@Test
	public void test_handleMessage_error() {

		ArrayList<PlugSim> plugs = new ArrayList<>();
		PlugSim pluga = new PlugSim("a");
		plugs.add(new PlugSim("a"));
		plugs.add(new PlugSim("b"));

		String topic = "prefis/action/a/random";
		MqttMessage msg = new MqttMessage("prueba2".getBytes());

		MqttCommands mqttCmd = new MqttCommands(plugs, "prueba2");
		mqttCmd.handleMessage(topic, msg);;
		assertFalse(pluga.isOn());
	}



}