package com.myprojects.marco.firechat.analytics;

/**
 * Created by marco on 07/08/16.
 */

public class DeveloperError extends Error {

    public DeveloperError(String detailMessage) {
        super(detailMessage);
    }

    public DeveloperError(String messageTemplate, Object... args) {
        super(String.format(messageTemplate, args));
    }

    public DeveloperError(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}

