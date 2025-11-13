package io.github.rothschil.web.controller;

import io.github.rothschil.domain.service.TblCdmaHlrService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Hidden
@Controller
public class BaseController {

    @Autowired
    protected TblCdmaHlrService tblCdmaHlrService;


}
