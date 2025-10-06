package org.study.yalivecoding.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class NotificationDto {
    private UUID id;
    private UUID receiver;
    private NotificationTypes type;
    private String message;
}
