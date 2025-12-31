package io.github.rothschil.common.initializer;

import io.github.rothschil.common.enums.SexEditor;
import io.github.rothschil.common.enums.SexEnum;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebBindingInitializer;


/**
* @description: TODO
* @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
* @version 1.0
*/
@ControllerAdvice
public class GlobalBindingInitializer implements WebBindingInitializer {

    @Override
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(SexEnum.class, new SexEditor());
    }
}