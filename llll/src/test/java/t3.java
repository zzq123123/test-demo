import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Package: PACKAGE_NAME
 * Description：
 * Author: wude
 * Date:  2020-12-03 22:29
 * Modified By:
 */
@Slf4j
public class t3 {

    @Test
    public void m() {
        Integer i1 = Integer.valueOf(5);  //直接去缓存拿个对象
        Integer i11 = 5;
        i1 = null;

        int i2 = i1.intValue();

        Boolean b = Boolean.TRUE;

    }


    @Test
    public void method1() {
        String a = String.valueOf('\u0000'); //控制台输出的是空格，\u0000 表示的是Unicode值
        log.info("a的Unicode值：" + Integer.toHexString(a.charAt(0)));// \u0000
        String b = " "; // 空格字符串
        String c = ""; //空字符串
        String d = null; //没有任何指向的字符串引用
        String e = "null"; //null字符串，这个null是常量池里的
        log.info(a + ";" + b + ";" + c + ";" + d + ";" + e + ";");
        log.info("a.equals(c):" + a.equals(c)); // false
        log.info("a.equals(b):" + a.equals(b)); // false
        log.info("a == c:" + (a == c)); // false
        log.info("a == d:" + (d == a)); // false
        log.info("a.equals(e):" + a.equals(e)); // false

    /*
      总结：1. 我们知道，Character类定了最小值 MIN_VALUE = '\u0000'，
              这也是ASCII表的最小值，这样描述：空字符（NUL） ''
            2. 虽然它转换为字符串输出为空格，但是它与空格、空字符串、NULL和"null"都不同，
              所以无法找到能够描述它的符号
     */

    }



    @Test
    public void m2(){
//        String a = " ";
//        log.info("a的Unicode值：" + Integer.toHexString(a.charAt(0)));// \u0000

   /*     String a = String.valueOf('\u0000'); //控制台输出的是空格，\u0000 表示的是Unicode值
        log.info("a的Unicode值：" + Integer.toHexString(a.charAt(0)));// \u0000
*/

        Integer integer = Integer.getInteger("name" );
        System.out.println(integer);
    }
}


