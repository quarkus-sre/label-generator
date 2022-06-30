package com.redhat.quarkus.integration.label.config;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

@Singleton
public class CustomConfiguration {

    // @ConfigProperty(name = "deployment.env")
    // String deploymentEnv;

    // /** Define common tags that apply only to a Prometheus Registry */
    // @Produces
    // @Singleton
    // @MeterFilterConstraint(applyTo = PrometheusMeterRegistry.class)
    // public MeterFilter configurePrometheusRegistries() {
    //     return MeterFilter.commonTags(Arrays.asList(
    //             Tag.of("registry", "prometheus")));
    // }

    // /** Define common tags that apply globally */
    // @Produces
    // @Singleton
    // public MeterFilter configureAllRegistries() {
    //     return MeterFilter.commonTags(Arrays.asList(
    //             Tag.of("env", deploymentEnv)));
    // }

    /** Enable histogram buckets for a specific timer */
    @Produces
    @Singleton
    public MeterFilter enableHistogram() {
        return new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                if(id.getName().equalsIgnoreCase("upsTimerRequest")) {
                    return DistributionStatisticConfig.builder()
                        .percentiles(0.5, 0.95)     // median and 95th percentile, not aggregable
                        .percentilesHistogram(true) // histogram buckets (e.g. prometheus histogram_quantile)
                        .build()
                        .merge(config);
                }
                return config;
            }
        };
    }
}
