package com.simon.microservice.microservicezuul.controller;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fengyue
 * @Date 2019-07-04 15:45
 */
@RestController
public class ErrorHandlerController implements ErrorController {

	@Autowired
	private ErrorAttributes errorAttributes;

	@Override
	public String getErrorPath() {
		return "/error";
	}


	@RequestMapping("/error")
    public  Map<String, Object> error(HttpServletRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request);
        String message = (String) errorAttributes.get("message");
        String trace = (String) errorAttributes.get("trace");
        if (StringUtils.isNotBlank(trace)) {
            message += String.format(" and trace %S", trace);

        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", 500);
        data.put("msg", message);
        return data;
    }

	private Map<String, Object> getErrorAttributes(HttpServletRequest request) {
		WebRequest requestAttributes = new ServletWebRequest(request);
		return errorAttributes.getErrorAttributes(requestAttributes, true);
	}
}
