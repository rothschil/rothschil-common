package io.github.rothschil.web.client;


import io.github.rothschil.domain.database.entity.TblCdmaHlr;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Map;

/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
public interface ApiClient {


    @GetExchange("/hlr/{phone}")
    Map hlr(@PathVariable(value = "phone") String phone);

}
