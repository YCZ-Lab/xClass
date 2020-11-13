package com.ysh.healthIndicator;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class MemorySpaceHealthIndicator extends AbstractHealthIndicator {
    OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.up();
        builder.withDetail("total(Java)", (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "MB")
                .withDetail("free(Java)", (Runtime.getRuntime().freeMemory() / 1024 / 1024) + "MB")
//                .withDetail("max(Java)", (Runtime.getRuntime().maxMemory() / 1024 / 1024) + "MB")
                .withDetail("total(System)", (osmb.getTotalPhysicalMemorySize() / 1024 / 1024) + "MB")
                .withDetail("free(System)", (osmb.getFreePhysicalMemorySize() / 1024 / 1024) + "MB")
                .withDetail("totalSwap(System)", (osmb.getTotalSwapSpaceSize() / 1024 / 1024) + "MB")
                .withDetail("freeSwap(System)", (osmb.getFreeSwapSpaceSize() / 1024 / 1024) + "MB")
                .withDetail("cpu", osmb.getSystemCpuLoad() + "%")
                .withDetail("loadAverage", osmb.getSystemLoadAverage() + "%");
    }
}
