package com.blog.admin.aspect;

import com.blog.admin.config.RecordLog;
import com.blog.admin.entity.Log;
import com.blog.admin.entity.WebMasterInfo;
import com.blog.admin.service.LogService;
import com.blog.admin.service.WebMasterInfoService;
import com.blog.admin.utils.DateTimeUtil;
import com.blog.admin.utils.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect
@Component
public class LogAspect {

    /**
     * 引入 mapper 接口 实现添加日志入库
     */
    @Autowired
    private LogService logService;

    @Autowired
    private WebMasterInfoService info;

    @Pointcut("@annotation(com.blog.admin.config.RecordLog)")//自定义元注解
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long time = endTime - beginTime;
        saveLog(point, time);
        return result;
    }

    public void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = new Log();
        //自定义元注解
        RecordLog logAnnotation = method.getAnnotation(RecordLog.class);
        if (logAnnotation != null) {
            // 注解上的描述
            log.setLogDec(logAnnotation.value());
        }
        // 请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.setLogMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = joinPoint.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            String params = "";
            for (int i = 0; i < args.length; i++) {
                params += "  " + paramNames[i] + ": " + args[i];
            }
            log.setLogParam(params);
        }

        //获取Request请求
        HttpServletRequest request = IPUtils.getHttpServletRequest();
        //设置IP地址
        String ip = IPUtils.getIP(request);
        WebMasterInfo masterInfo = info.getById(1);
        log.setLogIp(ip);
        log.setLogUser(masterInfo.getWmAccount());
        log.setLogRequireTime((int) time + "");
        log.setLogTime(DateTimeUtil.getCurrentDateTime());
        logService.save(log);
    }

}
