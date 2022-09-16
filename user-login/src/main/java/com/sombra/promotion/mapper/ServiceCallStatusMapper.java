package com.sombra.promotion.mapper;

import com.common.components.service.common.mapper.AbstractMapper;
import com.orca.dto.common.call_setings.ServiceCallStatusDTO;
import com.orca.entity.tenant.call_setings.ServiceCallStatus;
import org.springframework.stereotype.Component;

@Component
public class ServiceCallStatusMapper extends AbstractMapper<ServiceCallStatus, ServiceCallStatusDTO> {

    @Override
    public ServiceCallStatus fromDTO(ServiceCallStatusDTO callStatusDTO) {
        return ServiceCallStatus.createInstance(
                callStatusDTO.getId(),
                callStatusDTO.getName(),
                callStatusDTO.getStatusColor(),
                callStatusDTO.getIsDefault(),
                callStatusDTO.getDeleted());
    }

    @Override
    public ServiceCallStatusDTO toDTO(ServiceCallStatus callStatus) {
        return ServiceCallStatusDTO.createInstance(
                callStatus.getId(),
                callStatus.getName(),
                callStatus.getStatusColor(),
                callStatus.isDefault(),
                callStatus.isDeleted(),
                true);
    }
}
