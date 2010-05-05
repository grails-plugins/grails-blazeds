package org.epseelon.grails.blazeds.security

import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.security.annotation.Secured
import org.springframework.security.context.SecurityContextHolder
import org.springframework.security.AccessDeniedException
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Around

/**
 * @author sarbogast
 * @version 15 avr. 2010, 22:54:18
 */
@Aspect
class AuthorizationAspect {

    @Around(value="@annotation(secured)", argNames="secured")
    public Object invoke(ProceedingJoinPoint joinPoint, Secured secured) throws Throwable {
        String[] requiredAuthorities = secured.value()
        String[] actualAuthorities = SecurityContextHolder.getContext()?.authentication?.authorities?.authority
        String[] matchingAuthorities = requiredAuthorities?.toList()?.intersect(actualAuthorities?.toList())?.toArray(new String[0])
        if (matchingAuthorities?.length) {
            return joinPoint.proceed()
        } else {
            throw new AccessDeniedException("Access denied")
        }
    }
}
