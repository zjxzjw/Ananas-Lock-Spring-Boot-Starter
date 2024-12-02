package cn.zjx.ananas.aspect;

import cn.zjx.ananas.annotation.AnanasLock;
import cn.zjx.ananas.aspect.context.AnanasLockContext;
import cn.zjx.ananas.aspect.context.AnanasLockContextThreadLocal;
import cn.zjx.ananas.property.AnanasLockProperty;
import cn.zjx.ananas.utils.AnanasLockUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * 锁切面
 *
 * @author 钟家兴
 * @date 2024/12/2 14:08
 */
@Aspect
public class AnanasLockAspect implements Ordered {

    /**
     * 解析表达式
     */
    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * 参数名解析器
     */
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * 分隔符
     */
    private static final String SEPARATOR = ",";

    /**
     * 配置属性
     */
    private final AnanasLockProperty ananasLockProperty;

    /**
     * 锁工具类
     */
    private final AnanasLockUtil ananasLockUtil;

    /**
     * 切面顺序
     */
    private final int order;

    /**
     * 构造函数
     */
    public AnanasLockAspect(AnanasLockProperty ananasLockProperty,
                            AnanasLockUtil ananasLockUtil,
                            int order) {
        this.ananasLockProperty = ananasLockProperty;
        this.ananasLockUtil = ananasLockUtil;
        this.order = order;
    }

    /**
     * 获取切面顺序
     */
    @Override
    public int getOrder() {
        return order;
    }

    /**
     * 切点
     */
    @Pointcut("@annotation(cn.zjx.ananas.annotation.AnanasLock)")
    public void pointcut() {
    }

    /**
     * 前置通知
     */
    @Before("pointcut()")
    public void before() {
    }

    /**
     * 环绕通知
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        AnanasLock ananasLock = method.getAnnotation(AnanasLock.class);
        String[] keys = ananasLock.keys();

        Object[] args = joinPoint.getArgs();

        String key = assembleFullKey(methodSignature, args, keys);
        boolean locked = ananasLockUtil.tryLock(key, ananasLock.acquireTimeout(), TimeUnit.MICROSECONDS);
        if (!locked) {
            Class<?> exceptionClass = ananasLock.exception();
            Throwable throwable = (Throwable) exceptionClass.newInstance();
            Field messageField = Throwable.class.getDeclaredField("detailMessage");
            messageField.setAccessible(true);
            messageField.set(throwable, ananasLock.errorMsg());
            messageField.setAccessible(false);
            throw throwable;
        }

        AnanasLockContext context = new AnanasLockContext();
        context.setKey(key);
        AnanasLockContextThreadLocal.write(context);

        return joinPoint.proceed();
    }

    /**
     * 返回通知
     */
    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        unlockAndClear();
    }

    /**
     * 异常通知
     */
    @AfterThrowing(value = "pointcut()", throwing = "throwingValue")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwingValue) {
        unlockAndClear();
    }

    /**
     * 无法获得返回值，只能获得参数
     */
    @After("pointcut()")
    public void after() {
    }

    /**
     * 解锁并清除线程变量
     */
    private void unlockAndClear() {
        AnanasLockContext context = AnanasLockContextThreadLocal.read();
        ananasLockUtil.unlock(context.getKey());
        AnanasLockContextThreadLocal.clear();
    }

    /**
     * 组装锁的key
     */
    private String assembleFullKey(MethodSignature methodSignature, Object[] args, String[] keys) {
        Method method = methodSignature.getMethod();
        String methodGenericString = method.toGenericString();
        String[] strings = methodGenericString.split(" ");
        String uniqueMethod = strings[strings.length - 1];
        return ananasLockProperty.getKeyPrefix() + ":" + uniqueMethod + SEPARATOR + calculateKey(method, args, keys);
    }

    /**
     * 计算key
     */
    private String calculateKey(Method method, Object[] args, String[] definitionKeys) {
        EvaluationContext context = new MethodBasedEvaluationContext(
                null, method, args, NAME_DISCOVERER);

        List<String> definitionValueList = new ArrayList<>(definitionKeys.length);
        for (String definitionKey : definitionKeys) {
            if (definitionKey != null && !definitionKey.isEmpty()) {
                String key = PARSER.parseExpression(definitionKey).getValue(context, String.class);
                definitionValueList.add(key);
            }
        }

        StringJoiner stringJoiner = new StringJoiner(SEPARATOR);
        for (String value : definitionValueList) {
            stringJoiner.add(value);
        }
        return stringJoiner.toString();
    }
}
