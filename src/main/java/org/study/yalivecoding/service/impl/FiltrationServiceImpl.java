package org.study.yalivecoding.service.impl;

import org.study.yalivecoding.notification.HistoryRecordDto;
import org.study.yalivecoding.notification.NotificationDto;
import org.study.yalivecoding.notification.NotificationTypes;
import org.study.yalivecoding.repository.HistoryRepository;
import org.study.yalivecoding.repository.SettingRepository;
import org.study.yalivecoding.service.FiltrationService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class FiltrationServiceImpl implements FiltrationService {
    private final HistoryRepository historyRepository;
    private final SettingRepository settingRepository;

    public FiltrationServiceImpl(HistoryRepository historyRepository, SettingRepository settingRepository) {
        this.historyRepository = historyRepository;
        this.settingRepository = settingRepository;
    }

    private static boolean isCurrentDay(Date date) {
        Date twentyFourHoursAgoDate = Date.from(Instant.now().minus(24, ChronoUnit.HOURS));
        return date.after(twentyFourHoursAgoDate);
    }

    private static boolean isAllowed(NotificationDto notification,
                                     boolean senderIsAllowed,
                                     Collection<HistoryRecordDto> history,
                                     Set<NotificationTypes> allowedTypes) {

        return senderIsAllowed &&
                allowedTypes.contains(notification.getType()) &&
                history.stream().noneMatch(item ->
                        item.getId().equals(notification.getId()) &&
                                isCurrentDay(item.getTimestamp()));
    }

    @Override
    public Collection<NotificationDto> filter(UUID senderId, Collection<NotificationDto> incomingList) {
        return incomingList.stream()
                .collect(Collectors.groupingBy(NotificationDto::getReceiver))
                .entrySet().stream()
                .flatMap((entry) -> {
                    UUID receiverId = entry.getKey();
                    Collection<UUID> blockedSenders = settingRepository.blockedSender(receiverId);
                    Set<NotificationTypes> allowedTypes = settingRepository.allowedTypes(receiverId);
                    Collection<HistoryRecordDto> history = historyRepository.notificationHistory(receiverId);
                    List<NotificationDto> notificationsOfReceiver = entry.getValue();

                    boolean senderIsAllowed = !blockedSenders.contains(senderId);

                    return notificationsOfReceiver.stream().filter(
                            notification -> isAllowed(notification, senderIsAllowed, history, allowedTypes)
                    );
                })
                .toList();
    }
}
