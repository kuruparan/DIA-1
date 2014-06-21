package org.yarlithub.dia.repo;

import com.mysql.jdbc.Connection;
import org.yarlithub.dia.repo.object.Device;
import org.yarlithub.dia.repo.object.DeviceAccess;
import org.yarlithub.dia.repo.object.Garden;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project YIT DIA
 * Created by jaykrish on 5/25/14.
 */
public class DiaDBUtil {
    private static final Logger LOGGER = Logger.getLogger(DiaDBUtil.class.getName());

    /**
     * Create Device instance by device resultSet on current index.
     *
     * @param sql SQL String
     * @return device with positive id if successful, or id 0.
     */
    public static Device getDevice(String sql) {

        Device device = new Device();
        try {
            Connection con = DiaDBConnector.getConnection();
            ResultSet resultSet = sqlQuery(con, sql);
            if (resultSet.next()) {
                device.setId(resultSet.getInt("id"));
                device.setDeviceName(resultSet.getString("device_name"));
                device.setPin(resultSet.getString("pin"));
                device.setDeviceMask(resultSet.getString("device_mask"));
                device.setGardenId(resultSet.getInt("garden_id"));
                device.setOperationMode(resultSet.getInt("operation_mode"));
                device.setOperationType(resultSet.getInt("operation_type"));
                device.setCurrentStatus(resultSet.getInt("current_status"));
                device.setSchedule(resultSet.getString("schedule"));
                device.setSensorData(resultSet.getString("sensor_data"));
            }
            resultSet.close();
            con.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return device;
    }

    /**
     * Create List of Device instance by device resultSet.
     *
     * @param sql SQL String
     * @return List of Device with positive id if successful, or id 0.
     */
    public static List<Device> getDeviceList(String sql) {

        List<Device> deviceList = new ArrayList<Device>();
        try {
            Connection con = DiaDBConnector.getConnection();
            ResultSet resultSet = sqlQuery(con, sql);
            while(resultSet.next()) {
                Device device = new Device();
                device.setId(resultSet.getInt("id"));
                device.setDeviceName(resultSet.getString("device_name"));
                device.setPin(resultSet.getString("pin"));
                device.setDeviceMask(resultSet.getString("device_mask"));
                device.setGardenId(resultSet.getInt("garden_id"));
                device.setOperationMode(resultSet.getInt("operation_mode"));
                device.setOperationType(resultSet.getInt("operation_type"));
                device.setCurrentStatus(resultSet.getInt("current_status"));
                device.setSchedule(resultSet.getString("schedule"));
                device.setSensorData(resultSet.getString("sensor_data"));
                deviceList.add(device);
            }
            resultSet.close();
            con.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return deviceList;
    }

    /**
     * Create DeviceAccess instance by device resultSet on current index.
     *
     * @param sql SQL String
     * @return DeviceAccess with positive id if successful, or id 0.
     */
    public static DeviceAccess getDeviceAccess(String sql) {

        DeviceAccess deviceAccess = new DeviceAccess();
        try {
            Connection con = DiaDBConnector.getConnection();
            ResultSet resultSet = sqlQuery(con, sql);
            if (resultSet.next()) {
                deviceAccess.setId(resultSet.getInt("id"));
                deviceAccess.setDeviceId(resultSet.getInt("device_id"));
                deviceAccess.setUserMask(resultSet.getString("user_mask"));
                deviceAccess.setUserName(resultSet.getString("user_name"));
            }
            resultSet.close();
            con.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return deviceAccess;
    }

    /**
     * Create DeviceAccess list by device.
     *
     * @param sql SQL String
     * @return List of DeviceAccess if successful,else empty list.
     */
    public static List<DeviceAccess> getDeviceAccessList(String sql) {

        List<DeviceAccess> deviceAccessList = new ArrayList<DeviceAccess>();
        try {
            Connection con = DiaDBConnector.getConnection();
            ResultSet resultSet = sqlQuery(con, sql);
            while (resultSet.next()) {
                DeviceAccess deviceAccess =  new DeviceAccess();
                deviceAccess.setId(resultSet.getInt("id"));
                deviceAccess.setDeviceId(resultSet.getInt("device_id"));
                deviceAccess.setUserMask(resultSet.getString("user_mask"));
                deviceAccess.setUserName(resultSet.getString("user_name"));
                deviceAccessList.add(deviceAccess);
            }
            resultSet.close();
            con.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return deviceAccessList;
    }

    /**
     * Create Garden instance by device resultSet on current index.
     *
     * @param sql SQL String
     * @return Garden with positive id if successful, or id 0.
     */
    public static Garden getGarden(String sql) {

        Garden garden= new Garden();
        try {
            Connection con = DiaDBConnector.getConnection();
            ResultSet resultSet = sqlQuery(con, sql);
            if (resultSet.next()) {
                garden.setId(resultSet.getInt("id"));
                garden.setGardenName(resultSet.getString("garden_name"));
                garden.setPassword(resultSet.getString("password"));
            }
            resultSet.close();
            con.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException: " + e);
        }
        return garden;
    }

    /**
     * @param sql executeQuery the sql in Dia DB connection
     * @return resultSet or null
     */
    public static ResultSet sqlQuery(Connection con, String sql) throws SQLException {
        ResultSet resultSet = null;
        Statement stmt = con.createStatement();
        resultSet = stmt.executeQuery(sql);
        return resultSet;
    }

    /**
     * @param sql executeUpdate the sql in Dia DB connection
     * @return positive on success, or 0
     */
    public static int sqlUpdate(Connection con, String sql)  throws SQLException {
        int resultInt = 0;
            Statement stmt = con.createStatement();
            resultInt = stmt.executeUpdate(sql);
        return resultInt;
    }
}
