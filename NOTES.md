# Rate Limiter Algorithms — Comparison

## Sliding Window Log

- Sliding window uses rolling timeframe window to determine whether requests has hit limits
- Stricly caps requests within the timeframe window
- Consume more memory as it needs to remember all requests timeframe

## Token Bucket

- Token bucket relies on maximum requests capacity and is refillable with the set rate.
- Allow a bursts of requests within the capacity
- Low memory as it only tracks the amount of requests made

## When to Use Which

- **Sliding Window:** For strict limits you can't break — such as limiting login attempts or enforcing paid-tier quotas.
- **Token Bucket:** For APIs where some bursts are fine — it's the default choice for most cloud services.
