package cn.zjx.ananas.aspect.context;

public class AnanasLockContextThreadLocal {

    private AnanasLockContextThreadLocal() {
    }

    private static final ThreadLocal<AnanasLockContext> LOCK_CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static void clear() {
        LOCK_CONTEXT_THREAD_LOCAL.remove();
    }

    public static void write(AnanasLockContext context) {
        LOCK_CONTEXT_THREAD_LOCAL.set(context);
    }

    public static AnanasLockContext read() {
        AnanasLockContext context = LOCK_CONTEXT_THREAD_LOCAL.get();
        if (context == null) {
            context = new AnanasLockContext();
        }
        return context;
    }
}
