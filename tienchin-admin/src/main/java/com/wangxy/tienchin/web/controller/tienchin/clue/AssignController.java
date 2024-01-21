package com.wangxy.tienchin.web.controller.tienchin.clue;

import com.wangxy.tienchin.clue.domain.Assign;
import com.wangxy.tienchin.clue.service.IAssignService;
import com.wangxy.tienchin.common.core.controller.BaseController;
import com.wangxy.tienchin.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangxy
 * @since 2023-10-16
 */
@RestController
@RequestMapping("/clue/assign")
public class AssignController extends BaseController {
    @Autowired
    IAssignService assignService;

    @PostMapping
    @PreAuthorize("hasPermission('tienchin:clue:assign')")
    public AjaxResult assignClue(@Validated @RequestBody Assign assign){
        return  assignService.assignClue(assign);
    }
}
