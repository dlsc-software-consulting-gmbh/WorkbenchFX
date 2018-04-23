package uk.co.senapt.desktop.shell.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Globally defined loggers. Alternative approach to using class based loggers.
 */
public final class LoggingDomain {

    private static final String PREFIX = "uk.co.senapt.desktop.ui"; //$NON-NLS-1$

    /**
     * Logger used for anything related to the configuration of the calendar.
     */
    public static final Logger CONFIG = LoggerFactory.getLogger(PREFIX + ".config"); //$NON-NLS-1$

    /**
     * Logger used for anything related to the creation, fireing, and handling
     * of events.
     */
    public static final Logger EVENTS = LoggerFactory.getLogger(PREFIX + ".events"); //$NON-NLS-1$

    /**
     * Logger used for anything related to the model, adding / removing entries.
     */
    public static final Logger MODEL = LoggerFactory.getLogger(PREFIX + ".model"); //$NON-NLS-1$

    /**
     * Logger used for anything related to the creation of the view.
     */
    public static final Logger VIEW = LoggerFactory.getLogger(PREFIX + ".view"); //$NON-NLS-1$

    /**
     * Logger used for anything related to the creation of forms.
     */
    public static final Logger FORM = LoggerFactory.getLogger(PREFIX + ".form"); //$NON-NLS-1$

    /**
     * Logger used for the search service.
     */
    public static final Logger SEARCH = LoggerFactory.getLogger(PREFIX + ".search"); //$NON-NLS-1$

    /**
     * Logger used for anything related to performance.
     */
    public static final Logger PERFORMANCE = LoggerFactory.getLogger(PREFIX + ".performance"); //$NON-NLS-1$

    /**
     * Logger used for anything related to service invocations.
     */
    public static final Logger SERVICE = LoggerFactory.getLogger(PREFIX + ".service"); //$NON-NLS-1$
}
