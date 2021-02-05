import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Package: PACKAGE_NAME
 * Description：
 * Author: wude
 * Date:  2021-01-17 17:26
 * Modified By:
 */
@SpringBootTest
public class t11 {

    String  str = new String("hello");
    @Test
    public void m(){
        m1();
        System.out.println(str+"aha");

    }

    private void m1() {
        str = "kk";
    }


}
