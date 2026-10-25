public interface IRateLimiter {
    
    boolean allowRequest(String userId);

    long getRetryAfterMs(String userId);
}
