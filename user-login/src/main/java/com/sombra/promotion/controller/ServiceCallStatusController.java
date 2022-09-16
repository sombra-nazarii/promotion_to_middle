package com.sombra.promotion.controller;

import com.common.components.dto.anonymous.RestMessageDTO;
import com.common.components.util.ValidationUtil;
import com.orca.dto.common.call_setings.ServiceCallStatusDTO;
import com.orca.service.common.ServiceCallStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/dispatching-admin/service-call-status")
public class ServiceCallStatusController {

    @Autowired
    private ServiceCallStatusService serviceCallStatusService;

    @GetMapping
    public List<ServiceCallStatusDTO> getAllServiceCallStatuses() {
        return serviceCallStatusService.findAll();
    }

    @PostMapping
    public RestMessageDTO createServiceCallStatus(@RequestBody @Valid ServiceCallStatusDTO callStatusDTO) {
        ValidationUtil.validate(callStatusDTO);
        return serviceCallStatusService.createServiceCallStatusByDA(callStatusDTO);
    }

    @PutMapping
    public RestMessageDTO editServiceCallStatus(@RequestBody @Valid ServiceCallStatusDTO callStatusDTO) {
        ValidationUtil.validate(callStatusDTO);
        return serviceCallStatusService.editServiceCallStatusByDA(callStatusDTO);
    }

    @DeleteMapping(value = "/{id}")
    public RestMessageDTO deleteServiceCallStatus(@PathVariable("id") Long id) {
        return serviceCallStatusService.deleteServiceCallStatusByDA(id);
    }
}
