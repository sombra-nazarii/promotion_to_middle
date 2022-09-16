package com.sombra.promotion.repository;

import com.orca.entity.tenant.call_setings.ServiceCallPriority;
import com.orca.enums.CallPriorityIcon;
import com.orca.enums.RespondTimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceCallPriorityRepository extends JpaRepository<ServiceCallPriority, Long> {

    @Query(value = "SELECT c FROM ServiceCallPriority c WHERE c.deleted = FALSE")
    List<ServiceCallPriority> findAll();

    @Query(value = "SELECT c FROM ServiceCallPriority c WHERE c.name = :name AND c.deleted = FALSE")
    ServiceCallPriority findByNameNSD(@Param("name") String name);

    @Query(value = "SELECT c FROM ServiceCallPriority c " +
            "WHERE c.priorityColor = :color AND c.priorityIcon = :icon AND c.deleted = FALSE")
    ServiceCallPriority findByColorAndIconNSD(@Param("color") String color,
                                              @Param("icon") CallPriorityIcon icon);

    @Query(value = "SELECT c FROM ServiceCallPriority c WHERE c.id = :id AND c.deleted = FALSE")
    ServiceCallPriority findByIdNSD(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE ServiceCallPriority c SET c.deleted = TRUE WHERE c.id = :id")
    void deleteServiceCallPriorityById(@Param("id") Long id);

    @Query(value = "SELECT c FROM ServiceCallPriority c " +
            "WHERE c.respondHours = :hours AND c.respondTimeType = :type AND c.deleted = FALSE")
    ServiceCallPriority findByHoursAndTypeNSD(@Param("hours") Integer hours,
                                              @Param("type") RespondTimeType type);

    @Query(value = "SELECT c FROM ServiceCallPriority c WHERE c.priorityLevel = :priorityLevel AND c.deleted = FALSE")
    ServiceCallPriority findByPriorityLevelNSD(@Param("priorityLevel") Integer priorityLevel);

    @Query(value = "SELECT c FROM ServiceCallPriority c WHERE c.priorityLevel > :priorityLevel AND c.deleted = FALSE")
    List<ServiceCallPriority> findAllByPriorityLevelIsGreaterThanNSD(@Param("priorityLevel") Integer priorityLevel);
}
