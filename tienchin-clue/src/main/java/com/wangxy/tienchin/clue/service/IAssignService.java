package com.wangxy.tienchin.clue.service;

import com.wangxy.tienchin.clue.domain.Assign;
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

    AjaxResult assignClue(Assign assign);
}
