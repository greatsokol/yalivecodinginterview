package org.study.yalivecoding.repository;

import org.study.yalivecoding.notification.HistoryRecordDto;

import java.util.Collection;
import java.util.UUID;

public interface HistoryRepository {
    Collection<HistoryRecordDto> notificationHistory(UUID recepientId);
}
