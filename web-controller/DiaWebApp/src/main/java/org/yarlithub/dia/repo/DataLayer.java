package org.yarlithub.dia.repo;

import com.mysql.jdbc.Connection;
import org.yarlithub.dia.repo.object.Device;
import org.yarlithub.dia.repo.object.DeviceAccess;
import org.yarlithub.dia.repo.object.EndPoint;
import org.yarlithub.dia.repo.object.Garden;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project YIT DIA
 * Created by jaykrish on 5/30/14.
 */
public class DataLayer {
    private static final Logger LOGGER = Logger.getLogger(DataLayer.class.getName());

    public static Device getDeviceByMask(String device_mask) {
        String sql = String.format("SELECT * FROM device WHERE device_mask=\"%s\"", device_mask);
        return DiaDBUtil.getDevice(sql);
    }

    public static Device getDeviceByName(String device_name) {
        String sql = String.format("SELECT * FROM device WHERE device_name=\"%s\"", device_name);
        return DiaDBUtil.getDevice(sql);
    }

    public static Device getDeviceById(int id) {
        String sql = String.format("SELECT * FROM device WHERE id=%s", id);
        return DiaDBUtil.getDevice(sql);
    }

    public static DeviceAccess getDeviceAccessByMask(String user_mask) {
        String sql = String.format("SELECT * FROM device_access WHERE user_mask=\"%s\"", user_mask);
        return DiaDBUtil.getDeviceAccess(sql);
    }

    public static List<DeviceAccess> getDeviceAccessListByDevice(String device_id) {
        String sql = String.format("SELECT * FROM device_access WHERE device_id=\"%s\"", device_id);
        return DiaDBUtil.getDeviceAccessList(sql);
    }

    public static Garden getGardenByName(String garden_name) {
        String sql = String.format("SELECT * FROM garden WHERE garden_name=\"%s\"", garden_name);
        return DiaDBUtil.getGarden(sql);
    }

    public static List<Device> getDevicesByGardenId(int gardenId) {
        String sql = String.format("SELECT * FROM device where garden_id=\"%s\"", gardenId);
        return DiaDBUtil.getDeviceList(sql);
    }

    public static List<EndPoint> getEndPointsByDeviceId(int deviceId) {
        String sql = String.format("SELECT * FROM end_point where device_id=\"%s\"", deviceId);
        return DiaDBUtil.getEndPointList(sql);
    }

    public static EndPoint getEndPointById(int id) {
        String sql = String.format("SELECT * FROM end_point WHERE id=\"%s\"", id);
        return DiaDBUtil.getEndPoint(sql);
    }

    public static boolean isUser() {
        return false;
    }

    /**
     * get the maximum id of all the device and put a dummy value for new device to update.
     *
     * @return maximum id of all devices+1 , used to create new device name.
     */
    public static synchronized int reserveNewDevice() {
        int maxId = Integer.MAX_VALUE;

        Connection con = DiaDBConnector.getConnection();
        String sqlMaxId = "SELECT id FROM device ORDER BY id DESC LIMIT 1";
        try {
            ResultSet rs = DiaDBUtil.sqlQuery(con, sqlMaxId);
            if (rs.next()) {
                maxId = rs.getInt("id") + 1;
                String sqlIncrement =
                        String.format("INSERT INTO device (device_name, pin, device_mask)" +
                                " VALUES (\"%s\", \"reserved\", \"reserved\")"
                                , String.valueOf(maxId));
                DiaDBUtil.sqlUpdate(con, sqlIncrement);
            }
            rs.close();
            con.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            LOGGER.log(Level.SEVERE, "SQLException: " + se);
        }
        return maxId;
    }

    public static int updateNewDevice(Device device) {
        int result = 0;
        Connection con = DiaDBConnector.getConnection();
        String sql = String.format("UPDATE device "
                + "SET device_name = \"%s\", pin = \"%s\", device_mask = \"%s\""
                + "WHERE id = \"%s\""
                , device.getDeviceName(), device.getPin(), device.getDeviceMask(),String.valueOf(device.getId()));

        try {
            result = DiaDBUtil.sqlUpdate(con, sql);
            con.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return result;
    }

    public static int updateDevice(Device device) {
        int result = 0;
        Connection con = DiaDBConnector.getConnection();
        String sql = String.format("UPDATE device "
                + "SET garden_id = \"%s\""
                + "WHERE id = \"%s\""
                ,device.getGardenId(),String.valueOf(device.getId()));
        try {
            result = DiaDBUtil.sqlUpdate(con, sql);
            con.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return result;
    }
    public static int addEndPoint(int deviceId){
        int maxId = Integer.MAX_VALUE;
        int result=0;
        Connection con = DiaDBConnector.getConnection();
        String sqlMaxId = "SELECT id FROM end_point ORDER BY id DESC LIMIT 1";
        try {
            ResultSet rs = DiaDBUtil.sqlQuery(con, sqlMaxId);
            if (rs.next()) {
                maxId = rs.getInt("id") + 1;
                if(true){   //todo
                    String sqlIncrement =
                            String.format("INSERT INTO end_point (device_id)" +
                                    " VALUES (\"%s\")",
                                    deviceId, String.valueOf(maxId));
                    result= DiaDBUtil.sqlUpdate(con, sqlIncrement);
                }
            }
            rs.close();
            con.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            LOGGER.log(Level.SEVERE, "SQLException: " + se);
        }
        return result;
    }
    public static int updateEndPoint(EndPoint endPoint) {
        int result = 0;
        Connection con = DiaDBConnector.getConnection();
        String sql = String.format("UPDATE end_point "
                + "SET device_id = \"%s\", schedule = \"%s\", current_status = \"%d\", operation_mode = \"%d\", operation_type = \"%d\", sensor_data = \"%s\""
                + "WHERE id = \"%s\""
                ,endPoint.getDeviceId(),endPoint.getSchedule(),endPoint.getCurrentStatus(),endPoint.getOperationMode(),endPoint.getOperationType(),endPoint.getSensorData(), String.valueOf(endPoint.getId()));
        try {
            result = DiaDBUtil.sqlUpdate(con, sql);
            con.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return result;
    }

    public static int addNewGarden(Garden garden) {
        int result = 0;
        Connection con = DiaDBConnector.getConnection();
        String sql = String.format("INSERT INTO garden (garden_name, password)VALUES (\"%s\",\"%s\")"
                , garden.getGardenName(), garden.getPassword());
        try {
            result = DiaDBUtil.sqlUpdate(con, sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return result;
    }

    public static int insertDeviceAccess(int device_id, String user_mask) {
        int result = 0;
        Connection con = DiaDBConnector.getConnection();
        String sql = String.format("INSERT INTO device_access (device_id, user_mask)VALUES (\"%s\",\"%s\") "
                , String.valueOf(device_id), user_mask);
        try {
            result = DiaDBUtil.sqlUpdate(con, sql);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return result;
    }

}
