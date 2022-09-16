package com.sombra.promotion.mapper;

import com.common.components.dto.anonymous.RestMessageDTO;
import com.orca.dto.common.call_setings.ServiceCallStatusDTO;

import java.util.List;

public interface ServiceCallStatusService {

    List<ServiceCallStatusDTO> findAll();

    RestMessageDTO createServiceCallStatusByDA(ServiceCallStatusDTO serviceCallStatusDTO);

    RestMessageDTO editServiceCallStatusByDA(ServiceCallStatusDTO serviceCallStatusDTO);

    RestMessageDTO deleteServiceCallStatusByDA(Long id);

}
