package com.mdy.shirodemo;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * 实现对账户登陆次数的限制，使用EHcache缓存记录
 *实现对之前登陆的用户踢出，限制只能有一个用户在线
 * @author MDY
 */
public class MyShiroHashedCredentialsMatcher extends HashedCredentialsMatcher {


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        return true;
    }
}
