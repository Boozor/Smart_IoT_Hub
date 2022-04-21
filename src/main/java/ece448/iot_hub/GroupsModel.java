package ece448.iot_hub;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupsModel {
	@Autowired
	private PlugModel plugModel;

	public GroupsModel(PlugModel plugModel) {
		this.plugModel = plugModel;
	}

	private Map<String, List<String>> groups = new ConcurrentHashMap<>();

	synchronized public void publishAction(String group, String action, String plug) {

		if (plug != null) {
			plugModel.publishAction(plug, action);
			return;
		}
		List<String> list = groups.get(group);//list.isEmpty();
		if (null != list) {
			for (String plugName : list) {
				plugModel.publishAction(plugName, action);
			}
		}
	}

	synchronized public List<String> getGroups() {
		return new ArrayList<>(groups.keySet());
	}

	synchronized public List<String> getGroup(String group) {
		if (!groups.containsKey(group)) {
			return null;
		}
		return groups.get(group);
	}

	synchronized public void setGroup(String group, List<String> members) {
		groups.put(group, members);
	}

	synchronized public void removeGroup(String group) {
		groups.remove(group);
	}

	private static final Logger logger = LoggerFactory.getLogger(GroupsModel.class);
}