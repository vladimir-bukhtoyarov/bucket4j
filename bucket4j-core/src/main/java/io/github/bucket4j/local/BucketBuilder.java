/*
 *
 * Copyright 2015-2018 Vladimir Bukhtoyarov
 *
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package io.github.bucket4j.local;

import io.github.bucket4j.*;

import java.util.Objects;

/**
 * This builder creates in-memory buckets ({@link LockFreeBucket}).
 */
public class BucketBuilder {

    private final ConfigurationBuilder configurationBuilder;

    public BucketBuilder() {
        configurationBuilder = new ConfigurationBuilder();
    }

    /**
     * Adds limited bandwidth for all buckets which will be constructed by this builder.
     *
     * @param bandwidth limitation
     * @return this builder instance
     */
    public BucketBuilder addLimit(Bandwidth bandwidth) {
        configurationBuilder.addLimit(bandwidth);
        return this;
    }

    private TimeMeter timeMeter = TimeMeter.SYSTEM_MILLISECONDS;
    private SynchronizationStrategy synchronizationStrategy = SynchronizationStrategy.LOCK_FREE;
    private MathType mathType = MathType.INTEGER_64_BITS;

    /**
     * Specifies {@link TimeMeter#SYSTEM_NANOTIME} as time meter for buckets that will be created by this builder.
     *
     * @return this builder instance
     */
    public BucketBuilder withNanosecondPrecision() {
        this.timeMeter = TimeMeter.SYSTEM_NANOTIME;
        return this;
    }

    /**
     * Specifies {@link TimeMeter#SYSTEM_MILLISECONDS} as time meter for buckets that will be created by this builder.
     *
     * @return this builder instance
     */
    public BucketBuilder withMillisecondPrecision() {
        this.timeMeter = TimeMeter.SYSTEM_MILLISECONDS;
        return this;
    }

    /**
     * Specifies {@code customTimeMeter} time meter for buckets that will be created by this builder.
     *
     * @param customTimeMeter object which will measure time.
     *
     * @return this builder instance
     */
    public BucketBuilder withCustomTimePrecision(TimeMeter customTimeMeter) {
        if (customTimeMeter == null) {
            throw BucketExceptions.nullTimeMeter();
        }
        this.timeMeter = customTimeMeter;
        return this;
    }

    /**
     * Specifies {@code synchronizationStrategy} for buckets that will be created by this builder.
     *
     * @param synchronizationStrategy the strategy of synchronization which need to be applied to prevent data-races in multi-threading usage scenario.
     *
     * @return this builder instance
     */
    public BucketBuilder withSynchronizationStrategy(SynchronizationStrategy synchronizationStrategy) {
        if (synchronizationStrategy == null) {
            throw BucketExceptions.nullSynchronizationStrategy();
        }
        this.synchronizationStrategy = synchronizationStrategy;
        return this;
    }

    // TODO javadocs
    public BucketBuilder withMath(MathType mathType) {
        this.mathType = Objects.requireNonNull(mathType);
        return this;
    }

    /**
     * Constructs the bucket.
     *
     * @return the new bucket
     */
    public LocalBucket build() {
        BucketConfiguration configuration = configurationBuilder.build();

        for (Bandwidth bandwidth : configuration.getBandwidths()) {
            if (bandwidth.isIntervallyAligned() && !timeMeter.isWallClockBased()) {
                throw BucketExceptions.intervallyAlignedRefillCompatibleOnlyWithWallClock();
            }
        }
        switch (synchronizationStrategy) {
            case LOCK_FREE: return new LockFreeBucket(configuration, mathType, timeMeter);
            case SYNCHRONIZED: return new SynchronizedBucket(configuration, mathType, timeMeter);
            case NONE: return new SynchronizedBucket(configuration, mathType, timeMeter, FakeLock.INSTANCE);
            default: throw new IllegalStateException();
        }
    }

}