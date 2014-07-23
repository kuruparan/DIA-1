package org.yarlithub.dia.repo.object;

import org.yarlithub.dia.util.OperationMode;
import org.yarlithub.dia.util.OperationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Project YIT DIA
 * Created by jaykrish on 5/25/14.
 */
public class Device {
    private int id;
    private String deviceName;
    private String pin;
    private String deviceMask;
    private String sensorData;
    private String schedule;
    private int gardenId;
    private int operationMode;
    private int operationType;
    private int currentStatus;
    private List<EndPoint> endPointList = new ArrayList<>();

    public List<EndPoint> getEndPointList() {
        return endPointList;
    }

    public void setEndPointList(List<EndPoint> endPointList) {
        this.endPointList = endPointList;
    }

    public Device() {
        this.id = 0;
        this.operationMode = OperationMode.DEFAULT;
        this.operationType = OperationType.MANUAL;
        this.sensorData = "T:0;M:0";
        currentStatus = 0;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDeviceMask() {
        return deviceMask;
    }

    public void setDeviceMask(String deviceMask) {
        this.deviceMask = deviceMask;
    }

    public int getGardenId() {
        return gardenId;
    }

    public void setGardenId(int gardenId) {
        this.gardenId = gardenId;
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
