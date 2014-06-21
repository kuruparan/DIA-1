/**
 * Project YIT DIA
 * Created by jaykrish on 5/23/14.
 */

package org.yarlithub.dia.ussd;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Messages{
    private static final Logger LOGGER = Logger.getLogger(Messages.class.getName());

    public static final String USSD_BUNDLE_NAME = "org.yarlithub.dia.ussd.ussdmenu"; //$NON-NLS-1$
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(USSD_BUNDLE_NAME);


    private Messages()
    {
    }

    /**
     * <p>
     * Resolve a message by a key and argument replacements.
     * </p>
     * 
     * @see java.text.MessageFormat#format(String, Object...)
     * @param key
     *            the message to look up
     * @param arguments
     *            optional message arguments
     * @return the resolved message
     **/
    public static String getMessage(final String key, final Object... arguments)
    {
        try
        {
            if (arguments != null)
                return MessageFormat.format(resourceBundle.getString(key), arguments);
            return resourceBundle.getString(key);
        }
        catch (MissingResourceException e)
        {
            LOGGER.log(Level.ALL, "Message key not found: " + key);
            return "Sorry, internal error occurred, please exit and retry. \n\n000 Exit";
        }
    }

}
