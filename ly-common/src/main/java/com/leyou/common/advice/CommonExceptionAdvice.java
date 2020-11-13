package com.leyou.common.advice;

import com.leyou.common.exception.LyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionAdvice{
//   返回一个对象   创建对象的时候确定泛型 为Sring
//    只会打击运行时异常
    @ExceptionHandler(LyException.class)
    public ResponseEntity<String> handleerException(LyException e) {
//        错误信息没有断流  实际中为了避免这种断流(编译时异常不会触发@transctional这个回滚) 会进行编译转运行异常的操作    这个400写死了  需要通过自定义异常来解决掉




//        用方法返回值来代替写死
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());

    }
}
