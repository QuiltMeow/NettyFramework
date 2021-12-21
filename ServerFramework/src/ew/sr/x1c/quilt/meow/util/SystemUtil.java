package ew.sr.x1c.quilt.meow.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.NumberFormat;

public class SystemUtil {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();
    private static final String NEW_LINE = System.getProperty("line.separator");

    public static String getRuntimeData() {
        StringBuilder sb = new StringBuilder();

        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        ThreadMXBean tmb = ManagementFactory.getThreadMXBean();

        sb.append("本程式執行緒數量 : ").append(tmb.getThreadCount()).append(NEW_LINE);
        sb.append("JVM 執行緒數量 : ").append(tmb.getDaemonThreadCount()).append(NEW_LINE);
        sb.append("最大執行緒數量 : ").append(tmb.getPeakThreadCount()).append(NEW_LINE);
        sb.append("本程式最大記憶體 : ").append(maxMemory).append(" MB").append(NEW_LINE);
        sb.append("本程式可用記憶體 : ").append(freeMemory).append(" MB").append(NEW_LINE);
        sb.append("本程式總記憶體 : ").append(totalMemory).append(" MB").append(NEW_LINE);
        sb.append("系統物理記憶體總計 : ").append((osmb.getTotalPhysicalMemorySize() / 1024 / 1024)).append(" MB").append(NEW_LINE);
        sb.append("系統物理記憶體可用 : ").append((osmb.getFreePhysicalMemorySize() / 1024 / 1024)).append(" MB").append(NEW_LINE);
        sb.append("系統交換記憶體總計 : ").append((osmb.getTotalSwapSpaceSize() / 1024 / 1024)).append(" MB").append(NEW_LINE);
        sb.append("系統交換記憶體可用 : ").append((osmb.getFreeSwapSpaceSize() / 1024 / 1024)).append(" MB").append(NEW_LINE);

        NUMBER_FORMAT.setMaximumFractionDigits(3);
        sb.append("CPU 識別 : ").append(System.getenv("PROCESSOR_IDENTIFIER")).append(NEW_LINE);
        sb.append("CPU 可用核心數 : ").append(Runtime.getRuntime().availableProcessors()).append(NEW_LINE);
        sb.append("CPU 使用率 (本程式) : ").append(NUMBER_FORMAT.format((osmb.getProcessCpuLoad() * 100))).append(" %").append(NEW_LINE);
        sb.append("CPU 使用率 (系統) : ").append(NUMBER_FORMAT.format((osmb.getSystemCpuLoad() * 100))).append(" %").append(NEW_LINE);
        sb.append("CPU 時間 : ").append(osmb.getProcessCpuTime());
        return sb.toString();
    }
}
