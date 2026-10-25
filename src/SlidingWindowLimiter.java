import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class SlidingWindowLimiter implements IRateLimiter{

    final ConcurrentHashMap<String, LinkedList<Long>> requests = new ConcurrentHashMap<>();
    final int limit;
    final long windowUnit = 10000L;

    public SlidingWindowLimiter(int limit){
        this.limit = limit;
    }

    @Override
    public synchronized boolean allowRequest(String userId) {
        
        Long currentTime = System.currentTimeMillis();
        LinkedList<Long> userRequests = requests.computeIfAbsent(userId, k -> new LinkedList<>());

        while (!userRequests.isEmpty() && (currentTime - userRequests.peek() > windowUnit)) {
            userRequests.poll();
            System.out.println("Removed expired requests");
        }

        if (userRequests.size() < limit) {
            userRequests.offerFirst(currentTime);
            return true;
        }

        return false;
    }

    @Override
    public long getRetryAfterMs(String userId) {
        long retry = (requests.get(userId).peek() + windowUnit) - System.currentTimeMillis();
       
        System.out.println("Retry after: " + retry + "ms");
        return retry;
    }

}
