package com.sombra.promotion.mapper;

import com.common.components.dto.anonymous.RestMessageDTO;
import com.orca.dto.common.call_setings.ServiceCallPriorityDTO;
import com.orca.dto.common.call_setings.ServiceCallPriorityLevelDTO;

import java.util.List;

public interface ServiceCallPriorityService {

    List<ServiceCallPriorityDTO> findAll();

    RestMessageDTO createServiceCallPriorityByDA(ServiceCallPriorityDTO serviceCallStatusDTO);

    RestMessageDTO editServiceCallPriorityByDA(ServiceCallPriorityDTO serviceCallStatusDTO);

    RestMessageDTO deleteServiceCallPriorityByDA(Long id);

    RestMessageDTO editServiceCallPriorityLevelAndIconByDA(List<ServiceCallPriorityLevelDTO> dtoList);
}
