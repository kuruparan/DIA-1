package org.yarlithub.dia.repo.object;

import org.yarlithub.dia.util.OperationMode;
import org.yarlithub.dia.util.OperationType;

/**
 * Created by john on 7/4/14.
 */
public class EndPoint {
    private int id;
    private int deviceId;
    private int operationMode;
    private int operationType;
    private String schedule;
    private int currentStatus;
    private String sensorData;

    public EndPoint() {
        this.id = 0;
        this.operationMode = OperationMode.DEFAULT;
        this.operationType = OperationType.MANUAL;
        this.sensorData = "T:0;M:0";
        currentStatus = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(int operationMode) {
        this.operationMode = operationMode;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getSensorData() {
        return sensorData;
    }

    public void setSensorData(String sensorData) {
        this.sensorData = sensorData;
    }
}
