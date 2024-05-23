package com.gotsoccer.compare.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        ServletException exception = (ServletException) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (exception != null) {
            model.addAttribute("errorMessage", exception.getMessage());
        }
        return "error";
    }
}
