package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wei.broadband.common.DataMap;
import com.wei.broadband.common.R;
import com.wei.broadband.dto.DTO;
import com.wei.broadband.dto.MissionDTO;
import com.wei.broadband.mapper.MissionDao;
import com.wei.broadband.po.*;
import com.wei.broadband.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service("missionService")
public class MissionServiceImpl extends ServiceImpl<MissionDao, MissionEntity> implements MissionService {
    @Autowired
    private MissionService missionService;

    @Autowired
    private WalletItemService walletItemService;

    @Autowired
    private BroadbandCustomerService customerService;

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private SubCardService subCardService;

    @Autowired
    private MaintainService maintainService;

    @Autowired
    private UserService userService;

    @Autowired
    private DTO dto;


    @Autowired
    private DataMap dataMap;
    @Override
    public List<MissionDTO> getMissionList(String status, Integer userId, String mission, Integer isRoutine, Integer enable, Integer type) {
        LambdaQueryWrapper<MissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(status),MissionEntity::getStatus,status)
                .eq(userId!=null&&userId!=0,MissionEntity::getUserId,userId)
                .like(StringUtils.isNotEmpty(mission),MissionEntity::getDescription,mission)
                .eq(isRoutine!=null,MissionEntity::getIsRoutine,isRoutine)
                .eq(enable!=null,MissionEntity::getEnable,enable)
                .eq(type!=null,MissionEntity::getType,type);

        List<MissionEntity> list = missionService.list(queryWrapper);
        List<MissionDTO> missionDTOList = dto.missionDTO(list);
        return missionDTOList;
    }

    @Override
    public List<MissionDTO> getMyMissionList(Integer id, Integer type) {
        LambdaQueryWrapper<MissionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MissionEntity::getEnable,1)
                .eq(type!=null,MissionEntity::getType,type)
                .and((i)->i.isNull(MissionEntity::getUserId).or()
                        .eq(MissionEntity::getUserId,id));
        List<MissionEntity> list = missionService.list(queryWrapper);
        List<MissionDTO> missionDTOList = dto.missionDTO(list);
        return missionDTOList;
    }

    @Override
    public R<String> claimMission(Integer id, Integer missionId) {
        MissionEntity mission = missionService.getById(missionId);
        if (mission!=null){
            mission.setUserId(id);
            mission.setStatus("进行中");
            if (mission.getIsRoutine()==1){
                //常规任务
                mission.setId(0);
                missionService.save(mission);
            }else {
                //非常规任务
                missionService.updateById(mission);
            }

            return R.success("领取成功");
        }
        return R.error("操作有误,请检查登录情况");
    }

    @Override
    public R<String> updateMissionItem(MissionItem missionItem) {
        MissionItem savaItem = missionItem;
        savaItem.setEnable(1);



        //已存在则修改金额
        if (missionItem.getId()!=0){
            savaItem = walletItemService.getById(missionItem.getId());
            savaItem.setAmount(missionItem.getAmount());
        }else {
            //不存在则新建

            //需要套餐的类型为1,2,4
            int type = missionItem.getType();
            if (type==1||type==2||type==4){
                if (missionItem.getPackageId()==0){
                    return R.error("该类型任务需要设置套餐");
                }
            }else {
                missionItem.setPackageId(0);
            }

        }
        savaItem.setUpdateTime(LocalDateTime.now().toString());
        walletItemService.saveOrUpdate(savaItem);
        return R.success("更新成功");
    }

    @Override
    public R<String> addMission(MissionEntity mission) {
        MissionItem walletItem = walletItemService.getById(mission.getWalletItemId());
        mission.setDescription(walletItem.getDescription());
        mission.setAmount(walletItem.getAmount());
        mission.setPackageId(walletItem.getPackageId());
        mission.setEnable(1);
        if (mission.getUserId()==null){
            mission.setStatus("待领取");
        }
        else {
            mission.setStatus("进行中");
        }
        missionService.save(mission);
        return R.success("添加成功");
    }

    @Override
    public R<String> enableMission(int id) {
        MissionEntity mission = missionService.getById(id);
        mission.setEnable(1 - mission.getEnable());
        missionService.updateById(mission);


        return R.success("修改成功");
    }

    @Override
    public R<String> enableMissionItem(int id) {
        MissionItem missionItem = walletItemService.getById(id);
        missionItem.setEnable(1 - missionItem.getEnable());
        walletItemService.updateById(missionItem);
        return R.success("修改成功");
    }

    @Override
    public R<String> completeMission(int id, MissionForm form) {
        MissionEntity mission = missionService.getById(form.getMissionId());

        if (mission!=null&&mission.getEnable()==1&&mission.getStatus().equals("进行中")
                &&id!=0&&mission.getUserId()==id){
            String description="";
            int isComplete = 0;
            BroadbandCustomer cus = new BroadbandCustomer();
            BroadbandCustomer cusByPhone = dataMap.getPhone2CusMap().get(form.getPhone());
            BroadbandCustomer cusByBroadNumber = dataMap.getBroadNumber2CusMap().get(form.getBroadbandNumber());


            //处理任务类型1 移网套餐开户
            if (form.getType()==1){
                if (cusByPhone==null&&StringUtils.isNotEmpty(form.getPhone())
                        &&StringUtils.isNotEmpty(form.getName())){
                    cus.setPhone(form.getPhone());
                    cus.setName(form.getName());
                    cus.setAddress(form.getAddress());
                    cus.setPackageId(mission.getPackageId());
                    cus.setCreateUser(id);
                    customerService.save(cus);

                    description= form.getPhone()+mission.getDescription()+" 移网套餐开户";
                    isComplete = 1;
                }
                else {
                    return R.error("该用户已存在");
                }
            }



            //处理任务类型2 融合宽带开户
            else if (form.getType()==2){
                if (cusByPhone==null&&cusByBroadNumber==null&&StringUtils.isNotEmpty(form.getPhone())
                        &&StringUtils.isNotEmpty(form.getName())
                        &&StringUtils.isNotEmpty(form.getBroadbandNumber())){

                    cus.setPhone(form.getPhone());
                    cus.setBroadbandNumber(form.getBroadbandNumber());
                    cus.setName(form.getName());
                    cus.setLat(form.getLat());
                    cus.setLng(form.getLng());
                    cus.setImage(form.getImgName());
                    cus.setAddress(form.getAddress());
                    cus.setPackageId(mission.getPackageId());
                    cus.setCreateUser(id);
                    customerService.save(cus);

                    description= form.getPhone()+mission.getDescription()+" 融合宽带开户";
                    isComplete = 1;
                }
                else {
                    return R.error("该用户已存在或该宽带号已存在");
                }
            }



            //处理任务类型3 IPTV电视开户
            else if (form.getType()==3){
                if (cusByBroadNumber!=null&&cusByBroadNumber.getIptv()==0
                        &&StringUtils.isNotEmpty(form.getBroadbandNumber())){
                    cusByBroadNumber.setIptv(1);
                    cusByBroadNumber.setIptvDate(LocalDateTime.now());
                    customerService.updateById(cusByBroadNumber);

                    description= cusByBroadNumber.getPhone()+" "+cusByBroadNumber.getBroadbandNumber()+mission.getDescription()+" IPTV电视开户";
                    isComplete = 1;
                }
                else {
                    return R.error("不存在该用户，或已开启IPTV服务");
                }
            }

            //处理任务类型4 宽带+电视开户
            else if (form.getType()==4){

                if (cusByPhone==null&&cusByBroadNumber==null&&StringUtils.isNotEmpty(form.getPhone())
                        &&StringUtils.isNotEmpty(form.getName())
                        &&StringUtils.isNotEmpty(form.getBroadbandNumber())){

                    cus.setPhone(form.getPhone());
                    cus.setBroadbandNumber(form.getBroadbandNumber());
                    cus.setName(form.getName());
                    cus.setLat(form.getLat());
                    cus.setLng(form.getLng());
                    cus.setImage(form.getImgName());
                    cus.setAddress(form.getAddress());
                    cus.setPackageId(mission.getPackageId());
                    cus.setIptv(1);
                    cus.setIptvDate(LocalDateTime.now());
                    cus.setCreateUser(id);
                    customerService.save(cus);

                    description= form.getPhone()+mission.getDescription()+" 宽带+电视开户";
                    isComplete = 1;
                }
                else {
                    return R.error("该用户已存在");
                }


            }



            //处理任务类型5 携号转网开户
            else if (form.getType()==5){
                SubCardEntity subCard = subCardService.getOne(new LambdaQueryWrapper<SubCardEntity>()
                        .eq(SubCardEntity::getPhone, form.getSubCard()));
                if (cusByPhone!=null&&subCard==null&&StringUtils.isNotEmpty(form.getPhone())
                        &&StringUtils.isNotEmpty(form.getSubCard())){
                    SubCardEntity subCardEntity = new SubCardEntity();
                    subCardEntity.setCusId(cusByPhone.getId());
                    subCardEntity.setDateTime(LocalDateTime.now());
                    subCardEntity.setPhone(form.getSubCard());
                    subCardEntity.setDescription(form.getDescription());
                    subCardEntity.setIsCarrier(1);
                    subCardService.save(subCardEntity);

                    description= form.getPhone()+"添加携转副卡"+form.getSubCard();
                    isComplete = 1;
                }
                else {
                    return R.error("主卡号码不存在或副卡已存在");
                }
            }


            //处理任务类型6 客户维系
            else if (form.getType()==6){
                if (cusByPhone!=null&&StringUtils.isNotEmpty(form.getPhone())){
                    Maintain maintain =new Maintain();
                    maintain.setType(1);
                    maintain.setImage(form.getImgName());
                    maintain.setUserId(id);
                    maintain.setCustomerId(cusByPhone.getId());
                    maintain.setDescription(form.getDescription());
                    maintain.setDate(LocalDate.now());

                    maintainService.save(maintain);

                    description = form.getPhone()+"客户维系 "+form.getDescription();
                    isComplete = 1;
                }
                else {
                    return R.error("主卡号码不存在");
                }
            }




            //处理任务类型7 使用问题/故障上门服务
            else if (form.getType()==7){
                if (cusByPhone!=null&&StringUtils.isNotEmpty(form.getPhone())){
                    Maintain maintain =new Maintain();
                    maintain.setType(2);
                    maintain.setImage(form.getImgName());
                    maintain.setUserId(id);
                    maintain.setCustomerId(cusByPhone.getId());
                    maintain.setDescription(form.getDescription());
                    maintain.setDate(LocalDate.now());

                    maintain.setFailureType(form.getFailureType());

                    maintainService.save(maintain);

                    description = form.getPhone()+"使用问题/故障上门服务 "+form.getDescription();
                    isComplete = 1;
                }
                else {
                    return R.error("主卡号码不存在");
                }
            }




            //处理任务类型8 装移宽带
            else if (form.getType()==8){
                if (cusByPhone!=null&&cusByBroadNumber==null
                        &&StringUtils.isEmpty(cusByPhone.getBroadbandNumber())
                        &&StringUtils.isNotEmpty(form.getPhone())
                        &&StringUtils.isNotEmpty(form.getBroadbandNumber())){


                    cusByPhone.setBroadbandNumber(form.getBroadbandNumber());
                    cusByPhone.setLat(form.getLat());
                    cusByPhone.setLng(form.getLng());
                    cusByPhone.setImage(form.getImgName());
                    if (StringUtils.isNotEmpty(form.getAddress()))
                        cusByPhone.setAddress(form.getAddress());

                    customerService.updateById(cusByPhone);

                    description= form.getPhone()+mission.getDescription()+" 装移宽带"+form.getBroadbandNumber();
                    isComplete = 1;

                }
                else {
                    return R.error("该号码的用户不存在或已经有宽带号或该宽带号已存在");
                }
            }

            //处理任务类型9 添加副卡
            else if (form.getType()==9){
                SubCardEntity subCard = subCardService.getOne(new LambdaQueryWrapper<SubCardEntity>()
                        .eq(SubCardEntity::getPhone, form.getSubCard()));
                if (cusByPhone!=null&&subCard==null&&StringUtils.isNotEmpty(form.getPhone())
                        &&StringUtils.isNotEmpty(form.getSubCard())){
                    SubCardEntity subCardEntity = new SubCardEntity();
                    subCardEntity.setCusId(cusByPhone.getId());
                    subCardEntity.setDateTime(LocalDateTime.now());
                    subCardEntity.setPhone(form.getSubCard());
                    subCardEntity.setDescription(form.getDescription());
                    subCardEntity.setIsCarrier(0);
                    subCardService.save(subCardEntity);

                    description= form.getPhone()+"添加副卡"+form.getSubCard();
                    isComplete = 1;
                }
                else {
                    return R.error("主卡号码不存在或副卡已存在");
                }
            }



            //完成任务，添加钱包的收支记录
            if (isComplete==1&&mission.getAmount()!=0){

                PaymentsEntity paymentsEntity = new PaymentsEntity();
                paymentsEntity.setAmount(mission.getAmount());
                paymentsEntity.setDateTime(LocalDateTime.now());
                paymentsEntity.setUserId(id);
                paymentsEntity.setType("支出");
                paymentsEntity.setDescription(description);

                User user = dataMap.getId2UserMap().get(id);
                paymentsEntity.setPhone(user.getPhone());
                paymentsEntity.setUserName(user.getName());
                paymentsEntity.setStatus(1);
                paymentsService.save(paymentsEntity);

                user.setBalance(user.getBalance()+ paymentsEntity.getAmount());
                userService.updateById(user);
            }
            mission.setStatus("已完成");
            missionService.updateById(mission);
            return R.success("任务完成");

        }
        return R.error("出现错误，请联系管理员");
    }

    @Override
    public List<MissionItem> getWalletItemList(Integer packageId, Integer type, Integer enable) {
        LambdaQueryWrapper<MissionItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(packageId!=null,MissionItem::getPackageId,packageId)
                .eq(type!=null,MissionItem::getType,type)
                .eq(enable!=null,MissionItem::getEnable,enable);
        List<MissionItem> list = walletItemService.list(queryWrapper);
        return list;
    }
}