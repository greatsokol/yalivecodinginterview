package org.study.yalivecoding.service;

import org.study.yalivecoding.notification.NotificationDto;

import java.util.Collection;
import java.util.UUID;

public interface FiltrationService {
    Collection<NotificationDto> filter(UUID sender, Collection<NotificationDto> incoming);
}
