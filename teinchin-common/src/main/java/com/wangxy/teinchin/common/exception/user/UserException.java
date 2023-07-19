package com.wangxy.teinchin.common.exception.user;

import com.wangxy.teinchin.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author teinchin
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args) {
        super("user", code, args, null);
    }
}
