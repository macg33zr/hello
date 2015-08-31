package org.macg33zr.hello.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by bill on 31/08/2015.
 */
class RedirectInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // Open Shift proxy adds a header it it comes in over http. Try to redirect.
        String xForwardedProtoHeader = request.getHeader('x-forwarded-proto')
        if(xForwardedProtoHeader != null && request.getHeader('x-forwarded-proto')  == 'http') {
            log.info("Got header ${xForwardedProtoHeader}")
            String to = "https://${request.getServerName()}/${request.getRequestURI()}"
            //String to = "http://${request.getServerName()}:${request.getServerPort()}/trace"
            log.info("Redirect: ${to}")
            response.sendRedirect(to.toString())
            return true
        }
        log.info("No header 'x-forwarded-proto'")

        return true
    }

    @Override
    void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
