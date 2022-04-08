package me.fruits.fruits.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("me.fruits.fruits.mapper")
public class MybatisPlusConfiguration {

    @Bean
    public MybatisPlusInterceptor  paginationInterceptor() {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //mysql分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(500L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        //动态表插件
//        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
//        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
//            System.out.println("sql是: " + sql);
//            System.out.println("表名是: " + tableName);
//            return tableName;
//        });
//        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);

        return interceptor;
    }
}
