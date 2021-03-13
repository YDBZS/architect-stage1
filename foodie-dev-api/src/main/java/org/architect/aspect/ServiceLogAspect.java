package org.architect.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Service日志打印切面
 *
 * @author 多宝
 * @since 2021/3/13 21:02
 */
@Slf4j
@Aspect
@Component
public class ServiceLogAspect {



    /**
     *  切面表达式
     *  execution 代表所要执行的表达式主体
     *  第一处 * 代表方法返回类型 *代表所有类型
     *  第二处 包名代表AOP监控的类所在的包
     *  第三处 ..代表该包以及其子包下的所有类方法
     *  第四处 *代表类名，*代表所有类
     *  第五处 *(..) *代表类中的方法名 (..)表示方法中的任何参数
     *
     * @since 2021/3/13 21:22
     * @return java.lang.Object
     * @param joinPoint 执行主体
     */
    @Around("execution(* org.architect.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable{

        log.info("=================开始执行 {}.{}====================",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        // 记录开始时间
        Long begin = System.currentTimeMillis();

        // 执行目标Service
        Object result = joinPoint.proceed();

        Long end = System.currentTimeMillis();
        Long takeTime = end - begin;
        if (takeTime > 3000) {
            log.error("===========执行结束，耗时{}毫秒============", takeTime);
        } else if (takeTime > 2000) {
            log.warn("===========执行结束，耗时{}毫秒============", takeTime);
        }else {
            log.info("===========执行结束，耗时{}毫秒============", takeTime);
        }
        return result;
    }

}
