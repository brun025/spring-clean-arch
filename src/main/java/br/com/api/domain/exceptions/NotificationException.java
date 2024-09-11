package br.com.api.domain.exceptions;

import br.com.api.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    private static final long serialVersionUID = 1L;

	public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }
}
