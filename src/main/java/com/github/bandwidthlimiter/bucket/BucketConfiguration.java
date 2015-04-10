package com.github.bandwidthlimiter.bucket;

import static com.github.bandwidthlimiter.bucket.BucketExceptions.*;

public final class BucketConfiguration {

    private final RefillStrategy refillStrategy;
    private final Bandwidth[] bandwidths;
    private final boolean raiseErrorWhenConsumeGreaterThanSmallestBandwidth;
    private final TimeMeter timeMeter;

    public BucketConfiguration(Bandwidth[] bandwidths, boolean raiseErrorWhenConsumeGreaterThanSmallestBandwidth,
                               TimeMeter timeMeter, RefillStrategy refillStrategy) {
        if (timeMeter == null) {
            throw nullTimeMetter();
        }
        this.timeMeter = timeMeter;

        if (refillStrategy == null) {
            throw nullRefillStrategy();
        }
        this.refillStrategy = refillStrategy;

        Bandwidth.checkBandwidths(bandwidths);
        this.bandwidths = bandwidths;
        this.raiseErrorWhenConsumeGreaterThanSmallestBandwidth = raiseErrorWhenConsumeGreaterThanSmallestBandwidth;
    }

    public TimeMeter getTimeMeter() {
        return timeMeter;
    }

    public RefillStrategy getRefillStrategy() {
        return refillStrategy;
    }

    public Bandwidth[] getBandwidths() {
        return bandwidths;
    }

    public Bandwidth getBandwidths(int bandwidthIndex) {
        return bandwidths[bandwidthIndex];
    }

    public int getBandwidthCount() {
        return bandwidths.length;
    }

    public boolean isRaiseErrorWhenConsumeGreaterThanSmallestBandwidth() {
        return raiseErrorWhenConsumeGreaterThanSmallestBandwidth;
    }

}