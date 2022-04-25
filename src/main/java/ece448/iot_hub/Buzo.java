package ece448.iot_hub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
// import java.text.SimpleDateFormat;
import java.util.*;

public class Buzo {
        private Connection connection = null;
        private PreparedStatement ps_insertGroup = null;
        private PreparedStatement ps_deleteGroup = null;
        private PreparedStatement ps_getGroups = null;
        private PreparedStatement ps_getGroup = null;

        private PreparedStatement ps_insertMember = null;
        private PreparedStatement ps_deleteMembers = null;
        private PreparedStatement ps_getMembers = null;

        private PreparedStatement ps_insertPlug = null;
        private PreparedStatement ps_getPlugs = null;
        private PreparedStatement ps_getPlug = null;

        private PreparedStatement ps_insertPower = null;
       
        public Buzo(String dbName) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS groups (name STRING NOT NULL PRIMARY KEY)");
                
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS plugs (name STRING NOT NULL,"
                // +" date STRING NOT NULL, power REAL,"
                +"PRIMARY KEY(name) )");
               
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS groups_plugs "
                                       +"(groupName STRING, plugName STRING,"
                                       +"FOREIGN KEY(groupName) REFERENCES groups(name),"
                                       +"FOREIGN KEY(plugName) REFERENCES plugs(name),"
                                       +"PRIMARY key(groupName, plugName))");

            } catch(Exception e) { 
                logger.error(e.getMessage()); 
            }
        }
    
        public void createGroup(String groupName, List<String> plugsName) {
            // Error 19 is associated to the UNIQUE constraint
            // Create group if doesnt exist
            try {
                ps_getGroup = connection.prepareStatement("SELECT name FROM groups WHERE name=?");
                ps_getGroup.setString(1, groupName);
                ResultSet rs = ps_getGroup.executeQuery();
                if(!rs.next() ){
                    ps_insertGroup = connection.prepareStatement("INSERT INTO groups VALUES(?) ");
                    ps_insertGroup.setString(1, groupName);
                    ps_insertGroup.executeUpdate();
                }
            } catch(SQLException e) {
                logger.info("====insert groups");
                if(e.getErrorCode() != 19){logger.error("",e);}
            }
            // Delete plug members
            try { 
                ps_deleteMembers = connection.prepareStatement("DELETE FROM groups_plugs WHERE groupName=?");
                ps_deleteMembers.setString(1, groupName);
                ps_deleteMembers.executeUpdate();
            } catch(SQLException e) { 
                logger.error(e.getMessage()); 
            }
            // Add plug members
            for (String plug : plugsName) { 
                try {
                    ps_getPlug = connection.prepareStatement("SELECT name FROM plugs WHERE name=?");
                    ps_getPlug.setString(1, plug);
                    ResultSet rs = ps_getPlug.executeQuery();
                    if(!rs.next() ){
                        ps_insertPlug = connection.prepareStatement("INSERT INTO plugs VALUES(?)");
                        ps_insertPlug.setString(1, plug);
                        ps_insertPlug.executeUpdate();
                    }
                } catch(SQLException e) {
                    logger.info("====insert plugs");
                    if(e.getErrorCode() != 19){logger.error("",e);}
                }
                try {
                    ps_insertMember = connection.prepareStatement("INSERT INTO groups_plugs VALUES(?,?)");
                    ps_insertMember.setString(1, groupName);
                    ps_insertMember.setString(2, plug);
                    ps_insertMember.executeUpdate();
                } catch(SQLException e) {
                    if(e.getErrorCode() != 19){logger.error(e.getMessage());}
                }
            }
        }
    
        public void removeGroup(String groupName) {
            try {
                ps_deleteMembers = connection.prepareStatement("DELETE FROM groups_plugs WHERE groupName=?");
                ps_deleteMembers.setString(1, groupName);
                ps_deleteMembers.executeUpdate();
                ps_deleteGroup = connection.prepareStatement("DELETE FROM groups WHERE name=?");
                ps_deleteGroup.setString(1, groupName);
                ps_deleteGroup.executeUpdate();
            } catch(SQLException e) { logger.error(e.getMessage()); }
        }
    
        synchronized public List<String> getGroups() {
            List<String> groups = new ArrayList<String>();
            try {
                ps_getGroups = connection.prepareStatement("SELECT name FROM groups");
                ResultSet rs = ps_getGroups.executeQuery();
                while (rs.next()) {
                    groups.add(rs.getString("name"));
                }
            } catch(SQLException e) { logger.error(e.getMessage()); }
            return groups;
        }
        synchronized public List<String> getPlugs() {
            List<String> plugs = new ArrayList<String>();
            try {
                ps_getPlugs = connection.prepareStatement("SELECT name FROM plugs order by name");
                ResultSet rs = ps_getPlugs.executeQuery();
                while (rs.next()) {
                    plugs.add(rs.getString("name"));
                }
            } catch(SQLException e) { logger.error(e.getMessage()); }
            return plugs;
        }
    
        synchronized public List<String> getMembers(String group) {
            List<String> members = new ArrayList<String>();
            try {
                ps_getMembers = connection.prepareStatement("SELECT plugName FROM groups_plugs WHERE groupName=?");
                ps_getMembers.setString(1, group);
                ResultSet rs = ps_getMembers.executeQuery();
                while (rs.next()) {
                    members.add(rs.getString("plugName"));
                }
            } catch(SQLException e) { logger.error(e.getMessage()); }
            return members;
        }
    
        public void clear() {
            try {
                connection.createStatement().executeUpdate("DELETE FROM groups_plugs");
                connection.createStatement().executeUpdate("DELETE FROM groups");
                connection.createStatement().executeUpdate("DELETE FROM plugs");
            } catch(SQLException e) { logger.error(e.getMessage()); }
        }
        public void close() {
            try { ps_insertGroup.close(); } catch (Exception e) { logger.error(e.getMessage()); }
            try { ps_deleteGroup.close(); } catch (Exception e) { logger.error(e.getMessage()); }
            try { ps_getGroups.close(); } catch (Exception e) { logger.error(e.getMessage()); }
            try { ps_getGroup.close(); } catch (Exception e) { logger.error(e.getMessage()); }

            try { ps_deleteMembers.close(); } catch (Exception e) { logger.error(e.getMessage()); }
            try { ps_insertMember.close(); } catch (Exception e) { logger.error(e.getMessage()); }
            try { ps_getMembers.close(); } catch (Exception e) { logger.error(e.getMessage()); }

            try { ps_insertPlug.close(); } catch (Exception e) { logger.error(e.getMessage()); }
            try { ps_getPlugs.close(); } catch (Exception e) { logger.error(e.getMessage()); }
            try { ps_getPlug.close(); } catch (Exception e) { logger.error(e.getMessage()); }

            try { ps_insertPower.close(); } catch (Exception e) { logger.error(e.getMessage()); }
            try { connection.close(); } catch (Exception e) { logger.error(e.getMessage()); }
        }
    
        private static final Logger logger = LoggerFactory.getLogger(Buzo.class);
    
}
