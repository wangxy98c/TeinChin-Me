package com.wangxy.tienchin.clue.service;

import com.wangxy.tienchin.clue.domain.Clue;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangxy.tienchin.clue.domain.vo.ClueDetail;
import com.wangxy.tienchin.clue.domain.vo.ClueSummaryVO;
import com.wangxy.tienchin.clue.domain.vo.ClueVO;
import com.wangxy.tienchin.common.core.domain.AjaxResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangxy
 * @since 2023-10-16
 */
public interface IClueService extends IService<Clue> {

    AjaxResult addClue(Clue clue);

    List<ClueSummaryVO> selectClueList(ClueVO clueVO);

    AjaxResult getClueDetailByClueId(Integer clueId);

    AjaxResult clueFollow(ClueDetail clueDetail);

    AjaxResult invalidateClueFollow(ClueDetail clueDetail);

    AjaxResult getClueSummaryByClueId(Integer clueId);

    AjaxResult updateClue(Clue clue);

    Boolean deleteClueByIds(Long[] clueIds);

    AjaxResult clue2Bussiness(Integer clueId);
}
