package com.leyou.common.advice;

import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
//   注解的全类名  任意类 任意方法 都会被击中
@Around("within(@org.springframework.stereotype.Service *) || within(com.baomidou.mybatisplus.extension.service.IService+)") //目标方法的策略是环绕 打击 Iservice的子类
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


}
