package org.yarlithub.dia.repo.object;

/**
 * Project YIT DIA
 * Created by jaykrish on 5/25/14.
 */
public class DeviceAccess {
    private int id;
    private String userName;
    private String userMask;
    private int deviceId;

    public DeviceAccess() {
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMask() {
        return userMask;
    }

    public void setUserMask(String userMask) {
        this.userMask = userMask;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
