package ece448.iot_hub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class GroupsResource {
    @Autowired
    private GroupsModel groupsModel;
    @Autowired
    private PlugModel plugModel;

    public GroupsResource(GroupsModel groupsModel, PlugModel plugModel) {
        this.groupsModel = groupsModel;
        this.plugModel = plugModel;
    }

    @GetMapping("/api/groups/{group}")
    public Object getGroup(@PathVariable("group") String group,
            @RequestParam(value = "action", required = false) String action) {
        if (group == null) {
            // groups.publishState(group, action);
            return null;
        }
        if (action != "") {
            groupsModel.publishAction(group, action, null);
        }

        return makeGroup(group);
    }

    @PutMapping("/api/groups/{group}")
    public boolean controlGroup(@PathVariable("group") String group, @RequestBody Map<String, String> map) {
        if (group == null) {
            // groups.publishState(group, action);
            return false;
        }
        String action = map.get("action");
        if (action == null) {
            return false;
        }
        String plug = map.get("plug");
        groupsModel.publishAction(group, action, plug);
        return true;
    }

    @GetMapping("/api/groups")
    public Object getGroups() {
        List<Map<String, Object>> ret = new ArrayList<>();
        for (String groupName : groupsModel.getGroups()) {
            ret.add(makeGroup(groupName));
        }
        return ret;
    }

    @PostMapping("/api/groups/{group}")
    public void createGroup(@PathVariable("group") String group, @RequestBody List<String> members) {
        logger.info("REST Create group:{}, members:{} ", group, members.size());
        groupsModel.setGroup(group, members);
    }

    @DeleteMapping("/api/groups/{group}")
    public void deleteGroup(@PathVariable("group") String group) {
        logger.info("REST Delete group " + group);
        groupsModel.removeGroup(group);
    }

    protected Map<String, Object> makeGroup(String group) {
        // modify code below to include plug states
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("name", group);
        List<String> plugNames = groupsModel.getGroup(group);
        List<Map<String, Object>> members = new ArrayList<>();
        if (!CollectionUtils.isEmpty(plugNames)) {
            for (String plug : plugNames) {
                members.add(plugModel.getPlug(plug));
            }
        }
        groupMap.put("members", members);
        // logger.info("Plug {}: state {}, power {}", plug, plugs.getState(plug),
        // ret.get("power"));
        return groupMap;
    }

    private static final Logger logger = LoggerFactory.getLogger(GroupsResource.class);

}