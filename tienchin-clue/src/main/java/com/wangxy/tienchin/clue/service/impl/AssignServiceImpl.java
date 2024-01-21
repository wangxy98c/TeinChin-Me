package com.wangxy.tienchin.clue.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wangxy.tienchin.clue.domain.Assign;
import com.wangxy.tienchin.clue.mapper.AssignMapper;
import com.wangxy.tienchin.clue.service.IAssignService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangxy.tienchin.common.constant.TienchinConstants;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import com.wangxy.tienchin.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangxy
 * @since 2023-10-16
 */
@Service
public class AssignServiceImpl extends ServiceImpl<AssignMapper, Assign> implements IAssignService {
    @Override
    @Transactional
    public AjaxResult assignClue(Assign assign) {
         try {
             //将以前的线索分配记录latest设置为false
             UpdateWrapper<Assign> uw = new UpdateWrapper<>();
             uw.lambda().set(Assign::getLatest,false).eq(Assign::getAssignId,assign.getAssignId());
             update(uw);
             //分配
             assign.setType(TienchinConstants.CLUE_TYPE);
             assign.setCreateBy(SecurityUtils.getUsername());
             assign.setCreateTime(LocalDateTime.now());
             assign.setLatest(true);
             save(assign);
            return  AjaxResult.success("线索分配成功");
         }catch (Exception e){
            return AjaxResult.error("分配线索失败"+e.getMessage());
         }
    }
}
