package com.wei.broadband.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.MissionDTO;
import com.wei.broadband.po.MissionEntity;
import com.wei.broadband.po.MissionForm;
import com.wei.broadband.po.MissionItem;

import java.util.List;

/**
 * 
 *
 * @author wei
 * @email 
 * @date 2023-02-22 20:06:31
 */
public interface MissionService extends IService<MissionEntity> {


    List<MissionDTO> getMissionList(String status, Integer userId, String mission, Integer isRoutine, Integer enable, Integer type);

    List<MissionDTO> getMyMissionList(Integer id, Integer type);

    R<String> claimMission(Integer id, Integer missionId);

    R<String> updateMissionItem(MissionItem missionItem);

    R<String> addMission(MissionEntity mission);

    R<String> enableMission(int id);

    R<String> enableMissionItem(int id);

    R<String> completeMission(int id, MissionForm form);

    List<MissionItem> getWalletItemList(Integer packageId, Integer type, Integer enable);
}

