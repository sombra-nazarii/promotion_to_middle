package com.sombra.promotion.controller;

import com.common.components.dto.anonymous.RestMessageDTO;
import com.common.components.util.ValidationUtil;
import com.orca.dto.common.call_setings.ServiceCallPriorityDTO;
import com.orca.dto.common.call_setings.ServiceCallPriorityLevelDTO;
import com.orca.service.common.ServiceCallPriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/dispatching-admin/service-call-priority")
public class ServiceCallPriorityController {

    @Autowired
    private ServiceCallPriorityService serviceCallPriorityService;

    @GetMapping
    public List<ServiceCallPriorityDTO> getAllServiceCallPriorities() {
        return serviceCallPriorityService.findAll();
    }

    @PostMapping
    public RestMessageDTO createServiceCallPriority(@RequestBody @Valid ServiceCallPriorityDTO callPriorityDTO) {
        ValidationUtil.validate(callPriorityDTO);
        return serviceCallPriorityService.createServiceCallPriorityByDA(callPriorityDTO);
    }

    @PutMapping
    public RestMessageDTO editServiceCallPriority(@RequestBody @Valid ServiceCallPriorityDTO serviceCallPriorityDTO) {
        ValidationUtil.validate(serviceCallPriorityDTO);
        return serviceCallPriorityService.editServiceCallPriorityByDA(serviceCallPriorityDTO);
    }

    @DeleteMapping(value = "/{id}")
    public RestMessageDTO deleteServiceCallPriority(@PathVariable("id") Long id) {
        return serviceCallPriorityService.deleteServiceCallPriorityByDA(id);
    }

    @PatchMapping
    public RestMessageDTO editServiceCallPriorityLevelAndIcon(@RequestBody @Valid List<ServiceCallPriorityLevelDTO> dtoList) {
        ValidationUtil.validate(dtoList);
        return serviceCallPriorityService.editServiceCallPriorityLevelAndIconByDA(dtoList);
    }
}
