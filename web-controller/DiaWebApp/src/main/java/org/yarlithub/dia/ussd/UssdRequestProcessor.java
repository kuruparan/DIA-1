package org.yarlithub.dia.ussd;

import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.StatusCodes;
import hms.kite.samples.api.ussd.UssdRequestSender;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdResp;
import org.yarlithub.dia.repo.DataLayer;
import org.yarlithub.dia.repo.object.Device;
import org.yarlithub.dia.repo.object.DeviceAccess;
import org.yarlithub.dia.util.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Project YIT DIA
 * Created by jaykrish on 5/24/14.
 */
public class UssdRequestProcessor {

    private final static Logger LOGGER = Logger.getLogger(UssdHandler.class.getName());
    // hardcoded values
    private static final String EXIT_SERVICE_CODE = "000";
    private static final String BACK_SERVICE_CODE = "999";
    private static final String PROPERTY_KEY_PREFIX = "menu.level.";
    private static final String USSD_OP_MO_INIT = "mo-init";
    private static final String USSD_OP_MT_CONT = "mt-cont";
    private static final String USSD_OP_MT_FIN = "mt-fin";
    // menu state saving for back button
    private List<String> menuState = new ArrayList<>();
    // service to send the request
    private UssdRequestSender ussdMtSender;
    private Device device;
    private DeviceAccess deviceAccess;

    public UssdRequestProcessor(UssdRequestSender ussdMtSender) {
        this.ussdMtSender = ussdMtSender;
    }

    /**
     * Build the response based on the requested service code
     *
     * @param moUssdReq MO Ussd request.
     */
    public void processRequest(final MoUssdReq moUssdReq) throws SdpException {

        LOGGER.log(Level.INFO, "menuState array is now: " + menuState.toString());

        // exit request - session destroy
        if (moUssdReq.getMessage().equals(EXIT_SERVICE_CODE)) {
            createAndSendRequest(moUssdReq, "", USSD_OP_MT_FIN);
            return;// completed work and return
        }

        // back button handling
        if (moUssdReq.getMessage().equals(BACK_SERVICE_CODE)) {
            backButtonHandle(moUssdReq);
            return;// completed work and return
        }

        // get current service code
        String serviceCode = "";
        int wrongPinCount = 0;
        if (USSD_OP_MO_INIT.equals(moUssdReq.getUssdOperation())) {
            serviceCode = "1";
            clearMenuState();
            //first database check whether the requesting sim is device or user
            device = DataLayer.getDeviceByMask(moUssdReq.getSourceAddress());
            if (device.getId() > 0) {
                serviceCode = "2";
            }
            deviceAccess = DataLayer.getDeviceAccessByMask(moUssdReq.getSourceAddress());
            if (deviceAccess.getId()>0) {
                device= DataLayer.getDeviceById(deviceAccess.getDeviceId());
                serviceCode = "3";
            }
        } else {
            Boolean serviceCodeChanged = false;
            int currentMenuSize = menuState.size();
            String lastMenuState = menuState.get(menuState.size() - 1);
            if (currentMenuSize > 0 && lastMenuState.equals("1") && moUssdReq.getMessage().equals("1")) {
                //User is asking for configure as New Device, Generate new device id based on database.
                int newDeviceReservedId = DataLayer.reserveNewDevice();
                device = new Device();
                device.setId(newDeviceReservedId);
                device.setDeviceName("DIA" + String.valueOf(newDeviceReservedId));
            } else if (currentMenuSize > 0 && lastMenuState.equals("11")) {
                //User is entering a new pin for device.
                device.setPin(moUssdReq.getMessage());
                //message is user text so have to update service code manually
                serviceCode = "111";
                serviceCodeChanged = true;
            } else if (currentMenuSize > 0 && lastMenuState.equals("111")) {
                //User is entering pin again for confirmation for device.
                if (device.getPin().equals(moUssdReq.getMessage())) {
                    device.setDeviceMask(moUssdReq.getSourceAddress());
                    int results = DataLayer.updateNewDevice(device);
                    //terminate session by sending USSD_OP_MT_FIN
                    if (results == 1) {
                        createAndSendRequest(moUssdReq, buildMenuContent("1111"), USSD_OP_MT_FIN);
                    } else {
                        createAndSendRequest(moUssdReq, buildMenuContent("0"), USSD_OP_MT_FIN);
                    }
                    return;
                } else {
                    //entered pins does not match
                    //special case: redirect to state 11 again <= remove menuState
                    createAndSendRequest(moUssdReq, buildMenuContent("1112"), USSD_OP_MT_CONT);

                    menuState.remove(menuState.size() - 1);
                    return;
                }
            } else if (currentMenuSize > 0 && lastMenuState.equals("12")) {
                //User is entering device id he want to connect
                device = DataLayer.getDeviceByName(moUssdReq.getMessage());
                if (device.getId() > 0) {
                    //message is user text so have to update service code manually
                    serviceCode = "121";
                    serviceCodeChanged = true;
                } else {
                    //to display no such device
                    device.setDeviceName(moUssdReq.getMessage());
                    //no such device
                    //special case: redirect to state 12 again <= do not add menuState.
                    createAndSendRequest(moUssdReq, buildMenuContent("122"), USSD_OP_MT_CONT);
                    return;
                }
            } else if (currentMenuSize > 0 && lastMenuState.equals("121")) {
                //User is entering device pin he want to connect
                if (device.getPin().equals(moUssdReq.getMessage())) {
                    int results = DataLayer.insertDeviceAccess(device.getId(), moUssdReq.getSourceAddress());
                    //terminate session by sending USSD_OP_MT_FIN
                    if (results == 1) {
                        createAndSendRequest(moUssdReq, buildMenuContent("1211"), USSD_OP_MT_FIN);
                    } else {
                        createAndSendRequest(moUssdReq, buildMenuContent("0"), USSD_OP_MT_FIN);
                    }

                    return;
                } else {
                    //Wrong pin
                    //special case: redirect to state 121 again <= do not add menuState.
                    if (wrongPinCount <= 3) {
                        createAndSendRequest(moUssdReq, buildMenuContent("1212"), USSD_OP_MT_CONT);
                        return;
                    } else {
                        createAndSendRequest(moUssdReq, buildMenuContent("1213"), USSD_OP_MT_FIN);
                        return;
                    }
                }
            }
            if (!serviceCodeChanged) {
                serviceCode = getServiceCode(moUssdReq);
            }
            LOGGER.log(Level.INFO, "MoUssdRequest message: " + moUssdReq.getMessage());
            LOGGER.log(Level.INFO, "Service code: " + serviceCode);
        }
        // create request to display user
        createAndSendRequest(moUssdReq, buildMenuContent(serviceCode), USSD_OP_MT_CONT);

        // record menu state
        menuState.add(serviceCode);
    }

    /**
     * Build request object
     *
     * @param moUssdReq     - Receive request object
     * @param menuContent   - menu to display next
     * @param ussdOperation - operation
     * @return MtUssdReq - filled request object
     */
    private MtUssdResp createAndSendRequest(final MoUssdReq moUssdReq, final String menuContent, final String ussdOperation) throws SdpException {
        final MtUssdReq request = new MtUssdReq();
        request.setApplicationId(moUssdReq.getApplicationId());
        request.setEncoding(moUssdReq.getEncoding());
        request.setMessage(menuContent);
        request.setPassword(Property.getValue(moUssdReq.getApplicationId()));
        request.setSessionId(moUssdReq.getSessionId());
        request.setUssdOperation(Messages.getMessage("operation"));
        request.setVersion(moUssdReq.getVersion());
        request.setDestinationAddress(moUssdReq.getSourceAddress());

        // sending request to service
        MtUssdResp response = null;
        try {
            response = ussdMtSender.sendUssdRequest(request);
        } catch (SdpException e) {
            LOGGER.log(Level.INFO, "Unable to send request", e);
            throw e;
        }

        // response status
        String statusCode = response.getStatusCode();
        String statusDetails = response.getStatusDetail();
        if (StatusCodes.SuccessK.equals(statusCode)) {
            LOGGER.info("MT USSD message successfully sent");
        } else {
            LOGGER.info("MT USSD message sending failed with status code [" + statusCode + "] " + statusDetails);
        }
        return response;
    }

    /**
     * load a property from ussdmenu.properties
     *
     * @param key Key
     * @return value
     */
    private String getText(final String key) {
        return getPropertyValue(PROPERTY_KEY_PREFIX + key);
    }

    private String getPropertyValue(final String key) {
        //return key;
        return Messages.getMessage(key);
    }

    /**
     * Clear history list
     */
    private void clearMenuState() {
        LOGGER.info("clear history List");
        menuState.clear();
    }

    /**
     * Handlling back button with menu state
     *
     * @param moUssdReq MO Ussd request.
     * @throws hms.kite.samples.api.SdpException
     */
    private void backButtonHandle(final MoUssdReq moUssdReq) throws SdpException {
        String lastMenuVisited = "0";

        // remove last menu when back
        if (menuState.size() > 0) {
            menuState.remove(menuState.size() - 1);
            lastMenuVisited = menuState.get(menuState.size() - 1);
        }

        // create request and send
        createAndSendRequest(moUssdReq, buildMenuContent(lastMenuVisited), USSD_OP_MT_CONT);


        // clear menu status
        if (lastMenuVisited.equals("0")) {
            clearMenuState();
            // add 0 to menu state ,finally its in main menu
            menuState.add("0");
        }

    }

    /**
     * Create service code to navigate through menu and for property loading
     *
     * @param moUssdReq MO Ussd request.
     * @return serviceCode
     */
    private String getServiceCode(final MoUssdReq moUssdReq) {
        String serviceCode = "0";
        try {
            serviceCode = moUssdReq.getMessage();
        } catch (NumberFormatException e) {
            return serviceCode;
        }

        // create service codes for child menus based on the main menu codes

        if (menuState.size() > 0) {
            String generatedChildServiceCode = "" + menuState.get(menuState.size() - 1) + serviceCode;
            serviceCode = generatedChildServiceCode;
            LOGGER.info("generatedChildServiceCode is: " + generatedChildServiceCode);
        }

        return serviceCode;
    }

    /**
     * Build menu based on the service code
     *
     * @param selection
     * @return menuContent
     */
    private String buildMenuContent(final String selection) {
        String menuContent = "something went wrong!, please retry";
        try {
            // build menu contents
            menuContent = getText(selection);
            menuContent = String.format(menuContent, device.getDeviceName());

        } catch (MissingResourceException e) {
            // back to main menu
//            menuContent = getText("1");not possible to handle in our case
        }
        return menuContent;
    }
}
