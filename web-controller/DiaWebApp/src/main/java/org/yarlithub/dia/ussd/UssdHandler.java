/**
 * Project YIT DIA
 * Created by jaykrish on 5/23/14.
 */
package org.yarlithub.dia.ussd;


import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.ussd.MoUssdListener;
import hms.kite.samples.api.ussd.UssdRequestSender;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import org.yarlithub.dia.util.Property;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UssdHandler implements MoUssdListener {
    private final static Logger LOGGER = Logger.getLogger(UssdHandler.class.getName());

    private static final String USSD_OP_MO_INIT = "mo-init";
    private static final String EXIT_SERVICE_CODE = "000";

    //HashMap to keep simultaneous requests.
    private static volatile Map<String, UssdRequestProcessor> requestProcessorMap =
            new HashMap<String, UssdRequestProcessor>();
    // service to send the request
    private UssdRequestSender ussdMtSender;

    @Override
    public void init() {
        // create and initialize service
        try {
            LOGGER.log(Level.INFO, "Initiating UssdHandler....");
            ussdMtSender = new UssdRequestSender(new URL(Property.getValue("sdp.ussd.url")));
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred", e);
        }
    }

    /**
     * Receive requests
     *
     * @param moUssdReq MO Ussd request.
     */
    @Override
    public void onReceivedUssd(final MoUssdReq moUssdReq) {
        LOGGER.log(Level.INFO, "UssdHandler ussd request received.");

        UssdRequestProcessor ussdRequestProcessor;
        if (USSD_OP_MO_INIT.equals(moUssdReq.getUssdOperation())) {
            LOGGER.log(Level.INFO, "Creating new UssdRequestProcessor for MT INIT ");
            ussdRequestProcessor = new UssdRequestProcessor(ussdMtSender);
            requestProcessorMap.put(moUssdReq.getSessionId(), ussdRequestProcessor);
        } else {
            //else there should be session key, but for more safety double check and create new if not.
            if (!requestProcessorMap.containsKey(moUssdReq.getSessionId())) {
                LOGGER.log(Level.SEVERE, "Creating new UssdRequestProcessor, HasMap doest not contains!!!.");
                ussdRequestProcessor = new UssdRequestProcessor(ussdMtSender);
                requestProcessorMap.put(moUssdReq.getSessionId(), ussdRequestProcessor);
            } else {
                LOGGER.log(Level.INFO, "Using stored request processor to continue.");
                ussdRequestProcessor = requestProcessorMap.get(moUssdReq.getSessionId());
            }
        }
        try {
            ussdRequestProcessor.processRequest(moUssdReq);
        } catch (SdpException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred", e);
        }
        if (moUssdReq.getMessage().equals(EXIT_SERVICE_CODE)) {
            LOGGER.log(Level.INFO, "Removing request processor form HasMap on exit code.");
            requestProcessorMap.remove(moUssdReq.getSessionId());
        }
    }


}
