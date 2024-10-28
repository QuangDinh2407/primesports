package com.sportshop.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping("/access-denied")
    public String renderAD() {
        return "Error/access-denied";
    }

    @GetMapping("/page-not-found")
    public String renderPNF() {
        return "Error/page-not-found";
    }

    @GetMapping("/server-error")
    public String renderSE() {
        return "Error/server-error";
    }

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        String errorPage = "error"; // default
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorPage = "Error/page-not-found";

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // handle HTTP 403 Forbidden error
                errorPage = "Error/access-denied";
            }
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // handle HTTP 500 Internal Server error
                errorPage = "Error/server-error";
            }
        }
        return errorPage;
    }

}
