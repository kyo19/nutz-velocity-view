package org.nutzview.util;

import java.lang.reflect.Method;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.MethodInterceptor;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class HandleTimeInterceptor implements MethodInterceptor {
    
    @Override
    public void filter(InterceptorChain chain) throws Throwable {
        long start = System.currentTimeMillis();
        Method method = chain.getCallingMethod();
        Class<?> clazz = method.getDeclaringClass();
        String name = clazz.getSimpleName();
        String methodName = method.getName();
        String cmName = name + "." + methodName;
        chain.doChain();// 继续执行其他拦截器
        System.out.println("方法[" + cmName + "]执行耗时 -->" + (System.currentTimeMillis() - start) + "ms");
    }

}
