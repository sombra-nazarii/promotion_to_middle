package com.sombra.promotion.mapper;

import com.common.components.service.common.mapper.AbstractMapper;
import com.orca.dto.common.call_setings.ServiceCallPriorityDTO;
import com.orca.entity.tenant.call_setings.ServiceCallPriority;
import org.springframework.stereotype.Component;

@Component
public class ServiceCallPriorityMapper extends AbstractMapper<ServiceCallPriority, ServiceCallPriorityDTO> {

    @Override
    public ServiceCallPriority fromDTO(ServiceCallPriorityDTO callPriorityDTO) {
        return ServiceCallPriority.createInstance(
                callPriorityDTO.getId(),
                callPriorityDTO.getPriorityLevel(),
                callPriorityDTO.getName(),
                callPriorityDTO.getDescription(),
                callPriorityDTO.getPriorityColor(),
                callPriorityDTO.getPriorityIcon(),
                callPriorityDTO.getRespondHours(),
                callPriorityDTO.getRespondTimeType(),
                callPriorityDTO.getDeleted());
    }

    @Override
    public ServiceCallPriorityDTO toDTO(ServiceCallPriority callPriority) {
        return ServiceCallPriorityDTO.createInstance(
                callPriority.getId(),
                callPriority.getPriorityLevel(),
                callPriority.getName(),
                callPriority.getDescription(),
                callPriority.getPriorityColor(),
                callPriority.getPriorityIcon(),
                callPriority.getRespondHours(),
                callPriority.getRespondTimeType(),
                callPriority.isDeleted());
    }
}
