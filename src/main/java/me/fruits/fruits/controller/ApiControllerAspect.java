//package me.fruits.fruits.controller;
//
//
//import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
//import me.fruits.fruits.service.api.LoginApiModuleService;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.lang.annotation.Annotation;
//
//@Aspect
//@Component
//public class ApiControllerAspect {
//
//
//    @Autowired
//    private LoginApiModuleService loginApiModuleService;
//
//    /**
//     * 定义请求方法切入点
//     * 请求切点方法(已提供@RequestMapping,@GetMapping,@PostMapping注解，需要其它请增加)
//     */
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
//            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
//            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
//            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
//            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
//            "@annotation(org.springframework.web.bind.annotation.PatchMapping)"
//    )
//    void requestMapping() {
//    }
//
//
////    /**
////     * 接口是否需要登录的
////     */
////    @Around("requestMapping() ")
////    public Object needLogin(ProceedingJoinPoint pjp) throws Throwable {
////
////        //是否需要登录验证
////        boolean needLogin = false;
////
////        //反射获是否存在注解
////        Signature signature = pjp.getSignature();
////
////        //类中是否存在注解
////        Annotation annotationType = signature.getDeclaringType().getAnnotation(ApiLogin.class);
////        if(!ObjectUtils.isEmpty(annotationType)){
////            //类中存在ApiLogin注解
////            ApiLogin adminLoginType = (ApiLogin) annotationType;
////            needLogin = adminLoginType.isNeedLogin();
////        }else{
////            //类中不存在AdminLogin注解，查看当前调用的方法上是否存在注解
////            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
////            AdminLogin adminLoginMethod = methodSignature.getMethod().getAnnotation(AdminLogin.class);
////            if(!ObjectUtils.isEmpty(adminLoginMethod)){
////                needLogin = adminLoginMethod.isNeedLogin();
////            }
////        }
////
////        if(needLogin){
////            //todo:登录的token验证
////            loginAdminModuleService.injectJwtTokenContext();
////        }
////
////        // 方法运行之前
////        Object retVal = pjp.proceed();
////        // 方法运行之后
////        return retVal;
////    }
//
//}
