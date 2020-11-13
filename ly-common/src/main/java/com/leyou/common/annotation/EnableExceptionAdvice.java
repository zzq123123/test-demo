package com.leyou.common.annotation;

import com.leyou.common.advice.CommonExceptionAdvice;
import com.leyou.common.advice.CommonLogAdivice;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;
/**
 * @author 虎哥
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({CommonExceptionAdvice.class, CommonLogAdivice.class})
public @interface EnableExceptionAdvice {
}