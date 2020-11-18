package com.leyou.common.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.exception.LyException;
import com.sun.deploy.nativesandbox.comm.Request;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Target;

/**
 * Package: com.leyou.common.advice
 * Description：
 * Author: wude
 * Date:  2020-11-13 21:04
 * Modified By:
 */
@Slf4j
@Aspect //这个就是切面了 基础是动态代理  这个切面中存在着打击点和目标方法的关系
@Component //放入到容器中
public class CommonLogAdivice {
    private ObjectMapper objectMapper = new ObjectMapper();
//   注解的全类名  任意类 任意方法 都会被击中

    @Around("within(@org.springframework.stereotype.Service *) || within(com.baomidou.mybatisplus.extension.service.IService+)")
    //目标方法的策略是环绕 打击 Iservice的子类  代理对象 和 目标对象 的目标方法 也叫业务对象 和业务方法 打击的方法就是 目标方法 然后把通知方法(4种)织入到代理对象的方法中去 cglib只会生成一个对象，即Cglib生成的代理类的对象 原因就是代理对象是业务对象的内部类子类对象,jdk动态代理 有两个对象  @annotation关注方法 @args关注的是方法(参数列表)
    public Object handleExceptionLog(ProceedingJoinPoint jp) throws Throwable{
        try {

            log.debug("{}方法准备调用,参数:{}",jp.getSignature(), Arrays.toString(jp.getArgs()));
            long a = System.currentTimeMillis();
            Object result = jp.proceed();
            log.debug("{}方法调用成功,消耗时间{}ms",jp.getSignature(),System.currentTimeMillis()-a);
            return result;
        } catch (Throwable throwable) {
           log.error("{}方法失败,原因:{}",jp.getSignature(),throwable.getMessage(),throwable);
            if (throwable instanceof LyException) {
                throw throwable; //这个真是类型使我们自己定义的类型 直接交给 统一处理
            } else {
                //我们也要保证统一处理
                throw new LyException(500, throwable);
            }
        }
        //抓住异常必须抛出去 这里肯定不会过来了
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] parems = pjp.getArgs();
        Errors errors = null;
        for (Object parem : parems) {

            if (parem instanceof Errors) {
                errors = (Errors) parem;
            }
        }
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        RequestLog requestLog = new RequestLog();
        Enumeration<String> e = request.getHeaderNames();
        Map<String, Object> header = new HashMap();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            header.put(key, request.getHeader(key));

        }

        requestLog.setHeader(header);
        requestLog.setUrl(url);

        ;
        try {
            log.info(objectMapper.writeValueAsString(requestLog)+"=====================11111111111111111111111======================================="); // 编译时异常运行时异常事务会不会滚
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        if (errors != null && errors.hasErrors()) {
            List<ObjectError> errorList = errors.getAllErrors();
            StringBuilder sb = new StringBuilder();
            FieldError fieldError = (FieldError) errorList.get(0);
            sb.append(fieldError.getDefaultMessage());
            throw new LyException(400, sb.toString().trim());
        }

        try {
            Object proceed = pjp.proceed();


            return proceed;
        } catch (Throwable throwable) {
            if (throwable instanceof LyException) {
                throw throwable;//如果不是自定义异常直接抛给你html页面 不会是json
            } else {
                //我们也要保证统一处理
                throw new LyException(500, throwable);
            }
        }
    }

    @Data
    class RequestLog {

        String url;
        Map<String, Object> header;
        String ip;
        String clientName;
    }

}
