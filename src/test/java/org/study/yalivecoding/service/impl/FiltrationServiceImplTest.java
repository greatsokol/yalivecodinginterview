package org.study.yalivecoding.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.yalivecoding.notification.HistoryRecordDto;
import org.study.yalivecoding.notification.NotificationDto;
import org.study.yalivecoding.notification.NotificationTypes;
import org.study.yalivecoding.repository.HistoryRepository;
import org.study.yalivecoding.repository.SettingRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FiltrationServiceImplTest {
    @Mock
    HistoryRepository historyRepository;
    @Mock
    SettingRepository settingRepository;
    @InjectMocks
    FiltrationServiceImpl service;

    UUID allowedSender = UUID.randomUUID();
    UUID blockedSenderId1 = UUID.randomUUID();
    UUID blockedSenderId2 = UUID.randomUUID();
    UUID historyRecordId = UUID.randomUUID();

    Date fortyEightHoursAgoDate = Date.from(Instant.now().minus(48,ChronoUnit.HOURS));
    HistoryRecordDto historyRecord = new HistoryRecordDto(fortyEightHoursAgoDate, historyRecordId) ;

    Collection<UUID> blockedSenders;
    Collection<HistoryRecordDto> history;
    Set<NotificationTypes> allowedTypes;
    Collection<NotificationDto> notifications;

    @BeforeEach
    void setUp() {
        blockedSenders = new ArrayList<>(List.of(blockedSenderId1, blockedSenderId2));
        history = new ArrayList<>(List.of(historyRecord));
        allowedTypes = Set.of(NotificationTypes.SMS);
        notifications = new ArrayList<>(List.of(new NotificationDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                NotificationTypes.SMS,
                "Hello SMS"
        ), new NotificationDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                NotificationTypes.EMAIL,
                "Hello Email"
        ), new NotificationDto(
                historyRecordId,
                UUID.randomUUID(),
                NotificationTypes.SMS,
                "Hello already sent SMS"
        )));
    }

    @Test
    void filter_WhenBlockedSender_Filtered() {
        UUID senderId = blockedSenderId1;
        when(historyRepository.notificationHistory(any(UUID.class))).thenReturn(history);
        when(settingRepository.allowedTypes(any(UUID.class))).thenReturn(allowedTypes);
        when(settingRepository.blockedSender(any(UUID.class))).thenReturn(blockedSenders);

        Collection<NotificationDto> filteredList = service.filter(senderId, notifications);
        Assertions.assertEquals(0, filteredList.size());
    }

    @Test
    void filter_WhenAllowedSender_NotFiltered() {
        UUID senderId = allowedSender;
        when(historyRepository.notificationHistory(any(UUID.class))).thenReturn(history);
        when(settingRepository.allowedTypes(any(UUID.class))).thenReturn(allowedTypes);
        when(settingRepository.blockedSender(any(UUID.class))).thenReturn(blockedSenders);

        Collection<NotificationDto> filteredList = service.filter(senderId, notifications);
        Assertions.assertEquals(2, filteredList.size());
    }
}
