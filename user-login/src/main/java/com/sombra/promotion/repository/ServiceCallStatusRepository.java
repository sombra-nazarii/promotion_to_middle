package com.sombra.promotion.repository;

import com.orca.entity.tenant.call_setings.ServiceCallStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceCallStatusRepository extends JpaRepository<ServiceCallStatus, Long> {

    @Query(value = "SELECT c FROM ServiceCallStatus c WHERE c.deleted = FALSE")
    List<ServiceCallStatus> findAll();

    @Query(value = "SELECT c FROM ServiceCallStatus c WHERE c.name = :name AND c.deleted = FALSE")
    ServiceCallStatus findByName(@Param("name") String name);

    @Query(value = "SELECT c FROM ServiceCallStatus c WHERE c.statusColor = :color AND c.deleted = FALSE")
    ServiceCallStatus findByColor(@Param("color") String color);

    @Query(value = "SELECT c FROM ServiceCallStatus c WHERE c.id = :id AND c.deleted = FALSE")
    ServiceCallStatus findServiceCallStatusById(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE ServiceCallStatus c SET c.deleted = TRUE WHERE c.id = :id")
    void deleteServiceCallStatusById(@Param("id") Long id);

}
