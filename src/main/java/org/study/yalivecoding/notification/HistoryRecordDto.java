package org.study.yalivecoding.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class HistoryRecordDto {
    private Date timestamp;
    private UUID id;

}
