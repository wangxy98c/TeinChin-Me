package com.wangxy.tienchin.clue.mapper;

import com.wangxy.tienchin.clue.domain.Clue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangxy.tienchin.clue.domain.vo.ClueDetail;
import com.wangxy.tienchin.clue.domain.vo.ClueSummaryVO;
import com.wangxy.tienchin.clue.domain.vo.ClueVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wangxy
 * @since 2023-10-16
 */
public interface ClueMapper extends BaseMapper<Clue> {

    List<ClueSummaryVO> selectClueList(ClueVO clueVO);

    ClueDetail getClueDetailByClueId(Integer clueId);
}
