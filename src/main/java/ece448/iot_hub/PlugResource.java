package ece448.iot_hub;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

// import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;


@RestController
public class PlugResource {
    // @Autowired
    private PlugModel plugModel;

    // public PlugResource(){

    // }
    public PlugResource(PlugModel plugModel){
        this.plugModel=plugModel;
    }

    @GetMapping("/api/plugs")
    public List<Object> getPlugs()  {

        List<Object> ret = new ArrayList<>();

        for (String plug : plugModel.getPlugs()) {

            ret.add(makePlug(plug));

        }

        logger.info("Plugs: {}", ret);

        return ret;

    }

    @GetMapping("/api/plugs/{plug:.+}")
    public Object getPlug(

            @PathVariable("plug") String plug,

            @RequestParam(value = "action", required = false) String action)  {

        if (action != null) {

            plugModel.publishAction(plug, action);

        }

        // modify code below to control plugs by publishing messages to MQTT broker

        return makePlug(plug); // not null

    }

    @PostMapping("/api/plugs/{plug:.+}")

    public void createPlug(

            @PathVariable("plug") String plug,

            @RequestBody List<String> members) {

                plugModel.setPlugs(plug, members);

        logger.info("Plug {}: created {},{}", plug, members);

    }

    // @DeleteMapping("/api/plugs/{plug:.+}")

    // public void removePlug(

    // @PathVariable("plug") String plug) {

    // plugs.removePlug(plug);

    // logger.info("Plug {}: removed", plug);

    // }

    public Map<String, Object> makePlug(String plug) {

        // modify code below to include plug states  
        Map<String, Object> ret = new HashMap<>();

        ret.put("name", plug);

        ret.put("state", plugModel.getState(plug));
        // ret.put("power", "0");
        // if (plug.indexOf(".") != -1)

        // {

        //     ret.put("power", Integer.parseInt(plug.split("\\.")[1]));

        // }

        

        // logger.info("Plug {}: state {}, power {}", plug, plugModel.getState(plug), ret.get("power"));

        return ret;

    }

    private static final Logger logger = LoggerFactory.getLogger(PlugResource.class);

}