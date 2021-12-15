package me.fruits.fruits.configuration;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class CacheConfiguration {

    //菜单缓存 cacheManager和cache的名字定义
    public static final String menuCacheManager = "menuCacheManager";
    public static final String menuCacheManager_cache_menu = "menu";

    /**
     * @return 菜单的缓存管理
     */
    @Bean(value = menuCacheManager)
    public EhCacheCacheManager menuCacheManager() {


        CacheManager ehCacheCacheManager = CacheManager.newInstance();

        ehCacheCacheManager.addCache(new Cache(menuCacheManager_cache_menu, 1000, false, false, 30, 30));


        return new EhCacheCacheManager(ehCacheCacheManager);
    }
}

