package aop.stage0;

import aop.DataAccessException;
import aop.Transactional;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionHandler implements InvocationHandler {

    /**
     * @Transactional 어노테이션이 존재하는 메서드만 트랜잭션 기능을 적용하도록 만들어보자.
     */

    private final Object target;
    private final PlatformTransactionManager transactionManager;

    public TransactionHandler(final Object target, final PlatformTransactionManager transactionManager) {
        this.target = target;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final var declaredMethod = target.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
        if (declaredMethod.isAnnotationPresent(Transactional.class)) {
            final var transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
            try {
                final Object result = method.invoke(target, args);
                transactionManager.commit(transactionStatus);
                return result;
            } catch (InvocationTargetException | IllegalAccessException | RuntimeException e) {
                transactionManager.rollback(transactionStatus);
                throw new DataAccessException(e);
            }
        }
        return method.invoke(target, args);
    }
}
