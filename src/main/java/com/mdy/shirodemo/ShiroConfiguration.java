package com.mdy.shirodemo;


import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author MDY
 * shiro所需的配置类
 */
@Configuration
public class ShiroConfiguration {

    /**
     * 配置过滤器
     *
     * @param manager 装载shiro的安全管理器
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager manager) {
        // shiro的过滤器
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 自定义拦截器的配置
        Map<String, Filter> filter = new HashMap<>();

        filter.put("custom", new ShiroUserFilter());

        bean.setFilters(filter);
        // 安全管理器
        bean.setSecurityManager(manager);
        // 过滤器链配置
        // 配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 登出
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/**", "custom");
        filterChainDefinitionMap.put("/test", "user");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }


    /**
     * cookie对象;
     * rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     */
    private SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberME");
        //<!-- 记住我cookie生效时间7天 ,单位秒;-->
        //二级域名
//        simpleCookie.setDomain(".zsyyjs2018.com");
//        simpleCookie.setPath("/");
        simpleCookie.setMaxAge(6048000);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     */
    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("AztiX2RUqhc7dhOzl1Mj8Q=="));
        return cookieRememberMeManager;
    }


    /**
     * 配置核心安全事务管理器
     *
     * @param myShiroRealm 自定义的Reaml验证类
     */
    @Bean
    public SecurityManager securityManager(MyShiroRealm myShiroRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRememberMeManager(rememberMeManager());
        manager.setRealm(myShiroRealm);
        return manager;
    }


    /**
     * 配置自定义的权限登录器
     *
     * @param matcher 自定义密码验证器
     */
    @Bean
    public MyShiroRealm myShiroRealm(CredentialsMatcher matcher) {
        MyShiroRealm myRealm = new MyShiroRealm();
        myRealm.setCredentialsMatcher(matcher);
        return myRealm;
    }

    @Bean
    public CredentialsMatcher credentialsMatcher() {
        MyShiroHashedCredentialsMatcher myShiroHashedCredentialsMatcher = new MyShiroHashedCredentialsMatcher();
        myShiroHashedCredentialsMatcher.setHashAlgorithmName("MD5");
        myShiroHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return myShiroHashedCredentialsMatcher;
    }

    /**
     * 开启注解功能，验证失败， 其会抛出 UnauthorizedException异常，
     * 此时可以使用 Spring的 ExceptionHandler处理
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

    /**
     * 管理shiro bean生命周期
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 扫描上下文，寻找所有的Advistor(通知器），
     * 将这些Advisor应用到所有符合切入点的Bean中。
     * 所以必须在lifecycleBeanPostProcessor创建之后创建
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

}
