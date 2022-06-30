package com.redhat.quarkus.integration.label.config;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

@Singleton
public class CustomConfiguration {

    private long slo2nanoseconds = TimeUnit.NANOSECONDS.convert(2, TimeUnit.SECONDS);
    private long slo3nanoseconds = TimeUnit.NANOSECONDS.convert(3, TimeUnit.SECONDS);
    private long slo4nanoseconds = TimeUnit.NANOSECONDS.convert(4, TimeUnit.SECONDS);
    private long slo5nanoseconds = TimeUnit.NANOSECONDS.convert(5, TimeUnit.SECONDS);

    /** Enable histogram buckets for a specific timer */
    @Produces
    @Singleton
    public MeterFilter enableHistogram() {
        return new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                if(id.getName().equalsIgnoreCase("upsTimerRequest")) {
                    return DistributionStatisticConfig.builder()
                        .percentiles(0.5, 0.90, 0.95, 0.99)     // median, 90th, 95th, 99th percentile, not aggregable
                        .percentilesHistogram(true) // histogram buckets (e.g. prometheus histogram_quantile)
                        .serviceLevelObjectives(slo2nanoseconds, slo3nanoseconds, slo4nanoseconds, slo5nanoseconds) // fixed buckets in nanoseconds
                        .build()
                        .merge(config);
                }
                return config;
            }
        };
    }
}