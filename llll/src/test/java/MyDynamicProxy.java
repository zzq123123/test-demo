import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * Package: PACKAGE_NAME
 * Description：
 * Author: wude
 * Date:  2020-12-03 20:35
 * Modified By:
 */
public class MyDynamicProxy {

    public static void main(String[] args) {
        HelloImpl hello =  new HelloImpl();
        MyInvocationHandller handler = new MyInvocationHandller(hello);

                //构建代码示例
        Hello hello1 = (Hello) Proxy.newProxyInstance(MyDynamicProxy.class.getClassLoader(), new Class[]{Hello.class}, handler);
        System.out.println(hello1+"1111111111111111111111");
//        hello1.sayHello();  //代理类调用接口方法 - MyInvocationHandller#invoke方法 -invoke()#invoke()返回值  - 执行目标方法
    }



}

interface Hello{
    //大方法
     void sayHello();
}


class HelloImpl implements Hello{
    @Override
    public void sayHello() {
        System.out.println("Hello world");
         ;
    }
}
//代理类的方法中就执行了 MyInvocationHandller#invoke方法   invoke()#invoke()返回值??????

class MyInvocationHandller implements InvocationHandler {

    private Object target;

    public MyInvocationHandller(Object target) {
        this.target = target;
    }



        //中对象代理对象 接口方法对象   null 或者包装类
    @Override//这个返回值和接口方法要兼容
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里就是切面,
        System.out.println("Invoking sayHello");
        Object result = method.invoke(target,null);
        System.out.println(result+"22222222222222222");
        return null;
    }
}