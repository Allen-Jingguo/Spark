package com.ssc.ssgm.fx.ifx.integration.beans;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ConditionalOnClass({DataSource.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@AutoConfigureBefore(DataSourceTransactionManagerAutoConfiguration.class)
@MapperScan(basePackages = "com.ssc.ssgm.fx.ifx.integration.curd",sqlSessionTemplateRef = "sqlSessionTemplate")
public class DbConfigure implements ApplicationContextAware {

    private static Logger log = LoggerFactory.getLogger(DbConfigure.class);

    private  ApplicationContext ac;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource ds) throws Exception{
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/sqlmap/*.xml"));
        bean.setDataSource(ds);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        Properties properties = new Properties();
        properties.setProperty("helperDialect","oracle");
        PageInterceptor pageInterceptor = new PageInterceptor();
        pageInterceptor.setProperties(properties);
        configuration.addInterceptor(pageInterceptor);
        bean.setConfiguration(configuration);
        return bean.getObject();
    }

    @Bean
    @ConditionalOnMissingBean(PlatformTransactionManager.class)
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource ds){
        return  new DataSourceTransactionManager(ds);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory factory){
        return new SqlSessionTemplate(factory);
    }
}


