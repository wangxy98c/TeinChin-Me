package com.wangxy.tienchin.assign.service;

import com.wangxy.tienchin.assign.domain.Assign;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangxy.tienchin.common.core.domain.AjaxResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangxy
 * @since 2023-10-16
 */
public interface IAssignService extends IService<Assign> {

    AjaxResult assignClueOrBusiness(Assign assign);
}
