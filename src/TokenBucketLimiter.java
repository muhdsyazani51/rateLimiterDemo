public class TokenBucketLimiter implements IRateLimiter{

    int tokenCount, refillRate, capacity;
    Long bucketInitTime;

    public TokenBucketLimiter(int tokenCount, int refillRate, Long bucketInitTime) {
        this.tokenCount = tokenCount;
        this.capacity = tokenCount;
        this.refillRate = refillRate;
        this.bucketInitTime = bucketInitTime;
    }

    @Override
    public synchronized boolean allowRequest(String userId) {
        
        System.out.println("Capacity before refill: " + tokenCount);
        refillBucket();
        System.out.println("Capacity after refill: " + tokenCount);

        if(tokenCount > 0){
            tokenCount--;
            return true;
        }

        return false;
    }

    private void refillBucket(){
        Long currentTime = System.currentTimeMillis();
        long secondsElapse = (currentTime - bucketInitTime) / 1000;
        
        int refillAmount = (int) (secondsElapse * refillRate) + tokenCount;

        tokenCount = Math.min(capacity, refillAmount);

        bucketInitTime = currentTime;
    }

    @Override
    public long getRetryAfterMs(String userId) {
        return 1000;
    }

}
