package net.javaserver.celerlib;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = "celerlib", name = "celerlib")

public class CelerConfig {
    public static CachingCategory caching = new CachingCategory();
    public static OptimizationCategory optimization = new OptimizationCategory();
    static class CachingCategory {
        @Comment({"Controls the cache size for trigonometric functions. Set to 0 to disable.",
        "Memory consumed is approximately 36 more than 144 times this value, in bytes."})
        @RequiresMcRestart
        @RangeInt(min = 0, max = 500000)
        public int trigCacheSize = 8192;
    }
    static class OptimizationCategory {
        @Comment({"Set to false to disable optimizing basic trigonometric functions. Restart required to enable it.",
        "Also, setting trigCacheSize in the caching category will disable this optimization."})
        public boolean optimizeTrig = true;
        @Comment({"Set to false to disable concurrent cache sorting. Do NOT disable this if you are using a large cache size!!!"})
        @RequiresMcRestart
        public boolean concurrentCacheSorting = true;
    }
}
