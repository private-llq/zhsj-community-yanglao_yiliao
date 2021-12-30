package com.zhsj.community.yanglao_yiliao.myself.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.rpc.IBaseSmsRpcService;
import com.zhsj.baseweb.support.LoginUser;
import com.zhsj.community.yanglao_yiliao.common.entity.AgencySosEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilyRecordEntity;
import com.zhsj.community.yanglao_yiliao.common.entity.FamilySosEntity;
import com.zhsj.community.yanglao_yiliao.myself.mapper.AgencySosMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilyRecordMapper;
import com.zhsj.community.yanglao_yiliao.myself.mapper.FamilySosMapper;
import com.zhsj.community.yanglao_yiliao.myself.service.IFamilySosService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: zhsj-community-yanglao_yiliao
 * @description: sos家属信息
 * @author: Hu
 * @create: 2021-11-11 17:07
 **/
@Service
public class FamilySosServiceImpl extends ServiceImpl<FamilySosMapper, FamilySosEntity> implements IFamilySosService {

    @Resource
    private FamilySosMapper familySosMapper;

    @Resource
    private FamilyRecordMapper familyRecordMapper;

    @Resource
    private AgencySosMapper agencySosMapper;

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER)
    private IBaseSmsRpcService baseSmsRpcService;

    private static String URL;

    @Value("${select.agency.url}")
    public void setURL(String URL) {
        FamilySosServiceImpl.URL = URL;
    }

    @Override
    public void sos(LoginUser loginUser,Long familyId) {
        FamilyRecordEntity recordEntity = familyRecordMapper.selectById(familyId);

        List<FamilySosEntity> sosEntities = familySosMapper.selectList(new QueryWrapper<FamilySosEntity>().eq("uid", loginUser.getAccount()));
        if (sosEntities.size()!=0){
            //发送短信
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name",recordEntity.getName());
            for (FamilySosEntity sosEntity : sosEntities) {
                baseSmsRpcService.sendSms(sosEntity.getMobile(),"纵横世纪","SMS_229095430",hashMap);
            }
        }
        AgencySosEntity sosEntity = agencySosMapper.selectOne(new QueryWrapper<AgencySosEntity>().eq("uid", loginUser.getAccount()));
        if (sosEntity != null) {
            JSONObject agency = getAgency(sosEntity.getAgencyId());
            if (agency.get("code").equals("0")){
                JSONObject data = JSONObject.parseObject(JSON.toJSONString(agency.get("data")));

                //发送短信
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name1",recordEntity.getName());
                hashMap.put("name2",recordEntity.getName());
                //查询机构  发送短信
                baseSmsRpcService.sendSms(String.valueOf(data.get("shopPhone")),"纵横世纪","SMS_228851666",hashMap);
            }
        }
    }

    /**
     * @Description: 查询sos家属和机构信息
     * @author: Hu
     * @since: 2021/11/12 11:03
     * @Param: [loginUser]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> selectByUid(LoginUser loginUser,Long familyId) {
        Map<String, Object> map = new HashMap<>(2);
        //查询绑定家人
        List<FamilySosEntity> entityList = familySosMapper.selectList(new QueryWrapper<FamilySosEntity>().eq("uid", loginUser.getAccount()));
        if (entityList.size()!=0) {
            map.put("familyList",entityList);
        } else {
            map.put("familyList",null);
        }
        //查询绑定机构
        AgencySosEntity sosEntity = agencySosMapper.selectOne(new QueryWrapper<AgencySosEntity>().eq("uid", loginUser.getAccount()));
        if (sosEntity != null) {
            JSONObject jsonObject = getAgency(sosEntity.getAgencyId());
            System.out.println(jsonObject);
            if (!jsonObject.get("code").equals("0")){
                JSONObject data = JSONObject.parseObject(JSON.toJSONString(jsonObject.get("data")));
                data.put("id",sosEntity.getId());
                map.put("agency",data);
            } else {
                map.put("agency",null);
            }
        } else {
            map.put("agency",null);
        }
        return map;
    }

    /**
     * @Description: 查詢机构
     * @author: Hu
     * @since: 2021/12/9 15:57
     * @Param:
     * @return:
     */
    public static JSONObject getAgency(Long id) {
        String body=null;
        HttpClient httpClient = HttpClients.createDefault();
        String url = URL+"/shop/shop/newShop/getSupport/?shopId="+id;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-type", "application/json;charset=UTF-8");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            org.apache.http.HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity, "UTF-8");
            JSONObject jObject = JSONObject.parseObject(body);
            return jObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getAgency(1467745061635211265L));
    }



    /**
     * @Description: 查询sos家属和机构信息
     * @author: Hu
     * @since: 2021/11/12 11:03
     * @Param: [loginUser]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    @Override
    public Map<String, Object> selectUser(String uid) {
        Map<String, Object> map = new HashMap<>(2);
        //查询绑定家人
        List<FamilySosEntity> entityList = familySosMapper.selectList(new QueryWrapper<FamilySosEntity>().eq("uid", uid));
        if (entityList.size()!=0) {
            map.put("familyList",entityList);
        } else {
            map.put("familyList",null);
        }
        //查询绑定机构
        AgencySosEntity sosEntity = agencySosMapper.selectOne(new QueryWrapper<AgencySosEntity>().eq("uid", uid));
        if (sosEntity != null) {
            JSONObject jsonObject = getAgency(sosEntity.getAgencyId());
            map.put("agency",jsonObject.get("data"));
        } else {
            map.put("agency",null);
        }
        return map;
    }
}
