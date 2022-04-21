package ece448.iot_hub;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.junit.Test;

import ece448.iot_sim.MeasurePower;
import ece448.iot_sim.PlugSim;

public class PlugResourceTests {

	private static final String broker = "tcp://127.0.0.1";

	private static final String clientId = "iot_hub";

	private static final String topicPrefix = "iot_ece448";

	@Test

	public void test0() {

		MqttMessage msg = new MqttMessage();

		byte[] ret = msg.getPayload();

		String s = new String(ret, StandardCharsets.UTF_8);

		assertEquals(s, "");

	}

	@Test

	public void test1() {
		List<PlugSim> plugs = new ArrayList<>();
		PlugSim pSim = new PlugSim("a");
		plugs.add(pSim);
		pSim = new PlugSim("b");
		plugs.add(pSim);
		pSim = new PlugSim("c");
		plugs.add(pSim);
		MeasurePower mpPower = new MeasurePower(plugs);
		mpPower.start();
	}

	@Test

	public void test2() {

		PlugModel plugs;
		try {
			plugs = new PlugModel(broker, topicPrefix, clientId);
			PlugResource ret = new PlugResource(plugs);

			ret.createPlug("plug1", Arrays.asList("a", "b", "c"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test

	public void test3() {
		PlugModel plugs;
		try {
			plugs = new PlugModel(broker, topicPrefix, clientId);
			PlugResource ret = new PlugResource(plugs);

			ret.createPlug("plug2", Arrays.asList("a", "b", "c"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test

	public void test4() {

		PlugModel plugs;
		try {
			plugs = new PlugModel(broker, topicPrefix, clientId);
			PlugResource ret = new PlugResource(plugs);

			ret.createPlug("plug3", Arrays.asList("a", "b", "c"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test

	public void test5() {
		PlugModel plugs;
		try {
			plugs = new PlugModel(broker, topicPrefix, clientId);
			PlugResource ret = new PlugResource(plugs);

			ret.createPlug("plug4", Arrays.asList("a", "b", "c"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test

	public void test6() throws Exception {

		PlugModel plugs;
		try {
			plugs = new PlugModel(broker, topicPrefix, clientId);
			PlugResource ret = new PlugResource(plugs);

			ret.createPlug("plug5", Arrays.asList("a", "b", "c"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test

	public void test7() throws Exception {

		PlugModel plugs = new PlugModel(broker, topicPrefix, clientId);

		PlugResource ret = new PlugResource(plugs);

		// ret.createPlug("plug1", Arrays.asList("a", "b", "c"));

		ret.getPlug("a", "on");

		ret.getPlugs();
		ret.getPlug("a.777", null);

	}

	// MqttUpdate

	@Test

	public void test8() {

		PlugModel plugs = null;
		try {
			plugs = new PlugModel(broker, topicPrefix, clientId);
			PlugResource ret = new PlugResource(plugs);

			ret.getPlugs();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test

	public void test9() {

		PlugModel plugs = null;
		try {
			plugs = new PlugModel(broker, topicPrefix, clientId);
			PlugResource ret = new PlugResource(plugs);

			ret.getPlug("a", "on");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}