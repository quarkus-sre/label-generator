package com.redhat.quarkus.integration.label.config;

import java.time.Duration;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

@Singleton
public class CustomConfiguration {

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
                        .serviceLevelObjectives(0.5, 1.0, 2.5, 5.0)
                        .build()
                        .merge(config);
                }
                return config;
            }
        };
    }
}
