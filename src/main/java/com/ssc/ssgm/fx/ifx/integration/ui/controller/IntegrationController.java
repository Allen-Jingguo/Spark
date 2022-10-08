
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.common.Response;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@ApiOperation("api")
@Slf4j
public class IntegrationController {

    @ApiOperation("isRunning")
    @GetMapping("/isRunning")
    public Response<Boolean> isRunning() {
        return Response.success();
    }

    @ApiOperation("currentUser")
    @GetMapping("/currentUser")

    public Response<Boolean> currentUser() {
        return Response.success(true);
    }

}
