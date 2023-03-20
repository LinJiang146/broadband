package com.wei.broadband.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.DataMap;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.DTO;
import com.wei.broadband.dto.MissionDTO;


import com.wei.broadband.po.*;
import com.wei.broadband.service.*;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 *
 * @author wei
 * @email 
 * @date 2023-02-22 20:06:31
 */
@RestController
@RequestMapping("mission")
public class MissionController {

    @Autowired
    private MissionService missionService;


    @RequiresRoles("admin")
    @GetMapping("/list")
    public R<List<MissionDTO>> getMissionList(String status,Integer userId,String mission,Integer isRoutine,Integer enable,Integer type){

        List<MissionDTO> missionDTOList = missionService.getMissionList(status,userId,mission,isRoutine,enable,type);




        return R.success(missionDTOList);
    }

    @GetMapping("/getMyMissionList")
    public R<List<MissionDTO>> getMyMissionList(HttpServletRequest request,Integer type){
        Integer id = (int)request.getSession().getAttribute("user");
        List<MissionDTO> missionDTOList = missionService.getMyMissionList(id,type);


        return R.success(missionDTOList);
    }

    @GetMapping("claimMission")
    public R<String> claimMission(HttpServletRequest request,Integer missionId){
        Integer id = (int)request.getSession().getAttribute("user");
        return missionService.claimMission(id,missionId);





    }


    @GetMapping("getWalletItemList")
    public R<List<MissionItem>> getWalletItemList(Integer packageId, Integer type, Integer enable){
        List<MissionItem> list = missionService.getWalletItemList(packageId,type,enable);



        return R.success(list);
    }


    @PostMapping("updateWalletItem")
    public R<String> updateMissionItem(@RequestBody MissionItem missionItem){

        return missionService.updateMissionItem(missionItem);





    }


    @PostMapping("/addMission")
    public R<String> addMission(@RequestBody MissionEntity mission){
        return missionService.addMission(mission);




    }
    /**
     * 删除或启用
     */
    @DeleteMapping("enableMission")
    public R<String> enableMission(int id){

        return missionService.enableMission(id);

    }
    @DeleteMapping("enableMissionItem")
    public R<String> enableMissionItem(int id){

        return missionService.enableMissionItem(id);

    }

    @PostMapping("completeMission")
    public R<String> completeMission(HttpServletRequest request,@RequestBody MissionForm form){
        int id = (int)request.getSession().getAttribute("user");
        return missionService.completeMission(id,form);

    }
}
