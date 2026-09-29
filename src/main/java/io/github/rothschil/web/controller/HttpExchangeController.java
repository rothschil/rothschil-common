package io.github.rothschil.web.controller;


import io.github.rothschil.web.client.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/

@RestController
@RequestMapping(value = "/mock/test")
public class HttpExchangeController {

    @Autowired
    private ApiClient apiClient;


    @GetMapping(value = "/hlr2/{phone}")
    public Map hlr(@PathVariable(value = "phone") String phone) {
        return apiClient.hlr(phone);
    }
}
