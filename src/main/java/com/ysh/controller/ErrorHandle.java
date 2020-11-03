package com.ysh.controller;

import com.ysh.model.Logs;
import com.ysh.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Controller
public class ErrorHandle extends BasicErrorController {

    final LogsService logsService;

    public ErrorHandle(ErrorAttributes errorAttr, ServerProperties serverProp, List<ErrorViewResolver> errorViewResolvers, LogsService logsService) {
        super(errorAttr, serverProp.getError(), errorViewResolvers);
        this.logsService = logsService;
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest req, HttpServletResponse resp) {
        HttpStatus status = getStatus(req);
        Map<String, Object> model = getErrorAttributes(req, isIncludeStackTrace(req, MediaType.TEXT_HTML));
        ModelAndView mv = new ModelAndView("error", model, status);
        //////////////////////////////////////////////////////////////
        Logs logs = new Logs();
        String temp;
        logs.setRequestURI(req.getRequestURI());
        temp = req.getQueryString();
        if (temp == null) {
            temp = "";
        }
        logs.setQueryString(temp);
        logs.setMethod(req.getMethod());
        logs.setStatus(String.valueOf(resp.getStatus()));
        logs.setMessage(model.get("status") + ": " + model.get("error"));
        temp = req.getHeader("referer");
        if (temp == null) {
            temp = "";
        }
        logs.setReferer(temp);
        logs.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
        logs.setThread(Thread.currentThread().getName());
        logs.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        logs.setRemoteAddr(req.getRemoteAddr());
        logs.setRemotePort(String.valueOf(req.getRemotePort()));
        logs.setUserAgent(req.getHeader("User-Agent"));
        logs.setAcceptLanguage(req.getHeader("Accept-Language"));
        temp = req.getRequestedSessionId();
        if (temp == null) {
            temp = "";
        }
        logs.setSessionId(temp);
        temp = resp.getContentType();
        if (temp == null) {
            temp = "";
        }
        logs.setContentType(temp);
        logs.setConsumeTime(-1);
        logs.setSource("ErrorHandle");
        logsService.save(logs);
        //////////////////////////////////////////////////////////////
        return mv;
    }
}
