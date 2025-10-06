package org.study.yalivecoding.repository;

import org.study.yalivecoding.notification.NotificationTypes;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface SettingRepository {
    Set<NotificationTypes> allowedTypes(UUID recipientId);
    Collection<UUID> blockedSender(UUID recipientId);
}
