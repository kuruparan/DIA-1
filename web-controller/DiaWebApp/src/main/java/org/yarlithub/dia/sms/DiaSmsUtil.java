package org.yarlithub.dia.sms;

import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.sms.SmsRequestSender;
import hms.kite.samples.api.sms.messages.MoSmsReq;
import hms.kite.samples.api.sms.messages.MtSmsReq;
import hms.kite.samples.api.sms.messages.MtSmsResp;
import org.yarlithub.dia.repo.DataLayer;
import org.yarlithub.dia.repo.object.Device;
import org.yarlithub.dia.repo.object.DeviceAccess;
import org.yarlithub.dia.util.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project YIT DIA
 * Created by jaykrish on 5/31/14.
 */
public class DiaSmsUtil {
    private final static Logger LOGGER = Logger.getLogger(DiaSmsUtil.class.getName());

    public static String removeDIA(String message) {
        message = message.toLowerCase();
        if (message.startsWith("dia ")) {
            message = message.substring(4);
        } else {
            message = "INVALID";
        }
        return message;
    }

    /**
     * Prepare mt sms for list of controlling users from device request
     *
     * @param moSmsReq user moSms Request
     * @return
     */
    public static MtSmsReq createUserAlertMtSms(MoSmsReq moSmsReq) {
        MtSmsReq mtSmsReq = createDiaMtSms(moSmsReq);
        List<String> addressList = new ArrayList<String>();
        Device device = DataLayer.getDeviceByMask(moSmsReq.getSourceAddress());
        //TODO: get users list from db.
//        LOGGER.info("Device access check : deviceId " + deviceAccess.getDeviceId());
//        if (deviceAccess.getId() > 0) {
//            Device device = DataLayer.getDeviceById(deviceAccess.getDeviceId());
//            addressList.add(device.getDeviceMask());
//            LOGGER.info("Detected user's device : " + device.getDeviceName());
//        }
        mtSmsReq.setDestinationAddresses(addressList);
        return mtSmsReq;
    }

    /**
     * Prepare mt sms for user from device request
     *
     * @param moSmsReq user moSms Request
     * @return
     */
    public static MtSmsReq createUserReplyMtSms(MoSmsReq moSmsReq) {
        MtSmsReq mtSmsReq = createDiaMtSms(moSmsReq);
        List<String> addressList = new ArrayList<String>();
        addressList.add(moSmsReq.getSourceAddress());
        mtSmsReq.setDestinationAddresses(addressList);
        return mtSmsReq;
    }

    /**
     * Prepare mt sms for device from user request
     *
     * @param moSmsReq user moSms Request
     * @return
     */
    public static MtSmsReq createDeviceCommandMtSms(MoSmsReq moSmsReq) {
        MtSmsReq mtSmsReq = createDiaMtSms(moSmsReq);
        List<String> addressList = new ArrayList<String>();
        DeviceAccess deviceAccess = DataLayer.getDeviceAccessByMask(moSmsReq.getSourceAddress());
        LOGGER.info("Device access check : deviceId " + deviceAccess.getDeviceId());
        if (deviceAccess.getId() > 0) {
            Device device = DataLayer.getDeviceById(deviceAccess.getDeviceId());
            addressList.add(device.getDeviceMask());
            LOGGER.info("Detected user's device : " + device.getDeviceName());
        }
        mtSmsReq.setDestinationAddresses(addressList);
        return mtSmsReq;
    }

    /**
     * Prepare mt sms for device from device request
     *
     * @param moSmsReq device moSms Request
     * @return
     */
    public static MtSmsReq createDeviceReplyCommandMtSms(MoSmsReq moSmsReq) {
        MtSmsReq mtSmsReq = createDiaMtSms(moSmsReq);
        List<String> addressList = new ArrayList<String>();
        addressList.add(moSmsReq.getSourceAddress());
        LOGGER.info("Creating Device reply command SMS");
        mtSmsReq.setDestinationAddresses(addressList);
        return mtSmsReq;
    }

    private static MtSmsReq createDiaMtSms(MoSmsReq moSmsReq) {
        MtSmsReq mtSmsReq = new MtSmsReq();
        mtSmsReq.setApplicationId(moSmsReq.getApplicationId());
        mtSmsReq.setPassword(Property.getValue(moSmsReq.getApplicationId()));
        mtSmsReq.setVersion(moSmsReq.getVersion());
        String deliveryReq = moSmsReq.getDeliveryStatusRequest();
        if (deliveryReq != null) {
            if (deliveryReq.equals("1")) {
                mtSmsReq.setDeliveryStatusRequest("1");
            }
        } else {
            mtSmsReq.setDeliveryStatusRequest("0");
        }
        return mtSmsReq;
    }

    public static MtSmsResp sendCommand(SmsRequestSender smsMtSender, MtSmsReq mtSmsReq) {
        MtSmsResp mtSmsResp = null;
        List<String> addressList = mtSmsReq.getDestinationAddresses();
        try {
            //TODO:Multiple destination address is not-working/too-late, this is a workaround.
            for (String address : addressList) {
                List<String> addressListToSend = new ArrayList<>();
                addressListToSend.add(address);
                mtSmsReq.setDestinationAddresses(addressListToSend);
                mtSmsResp = smsMtSender.sendSmsRequest(mtSmsReq);
            }
        } catch (SdpException e) {
            LOGGER.log(Level.INFO, "Unexpected error occurred while sending SMS", e);
        }
        return mtSmsResp;
    }
}
