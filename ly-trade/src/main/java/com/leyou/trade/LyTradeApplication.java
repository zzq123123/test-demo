package com.leyou.trade;
        import com.leyou.common.annotation.EnableExceptionAdvice;
        import org.mybatis.spring.annotation.MapperScan;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.leyou.trade.mapper")
@EnableExceptionAdvice
 public class LyTradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyTradeApplication.class, args);
    }
}