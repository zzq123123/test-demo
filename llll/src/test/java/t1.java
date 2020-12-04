import org.junit.Test;
import org.junit.rules.ExternalResource;

/**
 * Package: PACKAGE_NAME
 * Description：
 * Author: wude
 * Date:  2020-12-03 17:40
 * Modified By:
 */
public class t1 {
    @Test
    public void m(){

        try {   // do something
            System.exit(1);
        }
        finally{  System.out.println("Print from finally");}

    }
    }



