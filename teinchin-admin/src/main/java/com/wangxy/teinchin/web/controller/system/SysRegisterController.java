package com.wangxy.teinchin.web.controller.system;

import com.wangxy.teinchin.common.core.controller.BaseController;
import com.wangxy.teinchin.common.core.domain.AjaxResult;
import com.wangxy.teinchin.common.core.domain.model.RegisterBody;
import com.wangxy.teinchin.common.utils.StringUtils;
import com.wangxy.teinchin.framework.web.service.SysRegisterService;
import com.wangxy.teinchin.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author teinchin
 */
@RestController
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService registerService;

    @Autowired
    private ISysConfigService configService;

    @PostMapping("/register")
    public AjaxResult register(@RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = registerService.register(user);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}
