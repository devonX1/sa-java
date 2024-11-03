package com.v1.sealert.sa.repo;

import com.v1.sealert.sa.model.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends BaseRepository<Notification, UUID> {

    @Query("Select n from Notification n where n.notificationDate >= :date")
    public List<Notification> getAllNotificationAfterDate(@Param("date") LocalDate localDate);

    public Optional<Notification> findByStreet(String street);

    public Optional<Notification> findByStreetAndNotificationPeriodAndNotificationDate(String street, String notificationPeriod, LocalDate localDate);

    public List<Notification> findByTownAndNotificationDateGreaterThanEqual(String town, LocalDate notificationDate);
}