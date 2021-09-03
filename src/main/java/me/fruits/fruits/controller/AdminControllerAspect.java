package me.fruits.fruits.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import me.fruits.fruits.service.admin.LoginService;
import me.fruits.fruits.utils.ErrCode;
import me.fruits.fruits.utils.FruitsException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

@Aspect
@Component
public class AdminControllerAspect {



    @Autowired
    private LoginService loginService;


    /**
     * 定义请求方法切入点
     * 请求切点方法(已提供@RequestMapping,@GetMapping,@PostMapping注解，需要其它请增加)
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping)"
    )
    void requestMapping() {
    }

    /**
     * 接口是否需要登录的
     */
    @Around("requestMapping() ")
    public Object needLogin(ProceedingJoinPoint pjp) throws Throwable {

        //是否需要登录验证
        boolean needLogin = false;

        //反射获是否存在注解
        Signature signature = pjp.getSignature();

        //类中是否存在注解
        Annotation annotationType = signature.getDeclaringType().getAnnotation(AdminLogin.class);
        if(!ObjectUtils.isEmpty(annotationType)){
            //类中存在AdminLogin注解
            AdminLogin adminLogin = (AdminLogin) annotationType;
            Login annotation = adminLogin.annotationType().getAnnotation(Login.class);
            needLogin = annotation.isNeedLogin();
        }else{
            //类中不存在AdminLogin注解，查看当前调用的方法上是否存在注解
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            AdminLogin adminLogin = methodSignature.getMethod().getAnnotation(AdminLogin.class);
            if(!ObjectUtils.isEmpty(adminLogin)){
                Login annotation = adminLogin.annotationType().getAnnotation(Login.class);
                needLogin = annotation.isNeedLogin();
            }
        }

        if(needLogin){
            //todo:登录的token验证
            boolean isLogin = loginService.injectJwtTokenContext();
            if(!isLogin){
                //登录失败
                //获取当前线程的servlet
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                HttpServletResponse response = servletRequestAttributes.getResponse();
                response.setStatus(401);
                return null;
            }
        }

        // 方法运行之前
        Object retVal = pjp.proceed();
        // 方法运行之后
        return retVal;
    }
}
