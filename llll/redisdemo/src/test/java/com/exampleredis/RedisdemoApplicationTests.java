package com.exampleredis;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class RedisdemoApplicationTests {
    String str1 = new String("Hello");

    @Test
    void contextLoads() {

        m1();
        System.out.println(str1);
    }
// java栈里面有栈帧
    private void m1() {
        str1 = "hh";
    }

    public static void main(String[] args) {
        System.out.println("111");
        m();
    }
    public static void m(){
        System.out.println("222");
        throw new RuntimeException("jaja");
    }
}