package com.redhat.quarkus.integration.label.config;

import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

@Singleton
public class CustomConfiguration {

    private long slo300ms = TimeUnit.NANOSECONDS.convert(300, TimeUnit.MILLISECONDS);
    private long slo400ms = TimeUnit.NANOSECONDS.convert(400, TimeUnit.MILLISECONDS);
    private long slo500ms = TimeUnit.NANOSECONDS.convert(500, TimeUnit.MILLISECONDS);
    private long slo1s = TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS);
    private long slo2s = TimeUnit.NANOSECONDS.convert(2, TimeUnit.SECONDS);

    /** Enable histogram buckets for a specific timer */
    @Produces
    @Singleton
    public MeterFilter enableHistogram() {
        return new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                if(id.getName().contains("TimerRequest")) {
                    return DistributionStatisticConfig.builder()
                        .percentiles(0.5, 0.90, 0.95, 0.99)     // median, 90th, 95th, 99th percentile, not aggregable
                        .percentilesHistogram(true) // histogram buckets (e.g. prometheus histogram_quantile)
                        .serviceLevelObjectives(slo300ms, slo400ms, slo500ms, slo1s, slo2s) // fixed buckets in nanoseconds
                        .build()
                        .merge(config);
                }
                return config;
            }
        };
    }
}