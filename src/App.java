public class App {
    public static void main(String[] args) throws Exception {
        IRateLimiter sLimiter = new SlidingWindowLimiter(100);
        IRateLimiter tLimiter = new TokenBucketLimiter(10, 1, System.currentTimeMillis());

        Requests thread1 = new Requests(tLimiter, "1");
        // Requests thread2 = new Requests(sLimiter, "2");

        thread1.start();
        // thread2.start();
    }
}

class Requests extends Thread {

    IRateLimiter limiter = null;
    String userId = null;

    Requests(IRateLimiter limiter, String userId){
        this.limiter = limiter;
        this.userId = userId;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 150; i++) {

            boolean success = limiter.allowRequest(userId);
            while (!success) {
                System.out.println("Thread "+Thread.currentThread().getName() +" - Login attempt " + (i) + " Blocked.");
                try {
                    Thread.sleep(limiter.getRetryAfterMs(userId));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                success = limiter.allowRequest(userId);
            }
            
            System.out.println("Thread "+Thread.currentThread().getName() +" - Login attempt " + (i) + " Allowed.");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
