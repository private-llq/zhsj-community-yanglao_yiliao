package com.zhsj.community.yanglao_yiliao.old_activity.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * @author yu
 */
@Component
public class GouldUtil {

    /**
     * 高德地图key
     */
    private static final String GOULD_KEY = "eded4a3549c993f8dbc2a20072dbcf27";
    private static final int R = 6371;
    private static final String GEOCODES = "geocodes";
    private static final String REGEOCODES = "regeocode";
    private static final String FROMATTED_ADDRESS = "formatted_address";
    private static final String LOCATION = "location";
    private static final String STATUS = "status";
    private static final String STR_1 = "1";
    private static final String ARRY = "[]";

    //申请的账户Key

    /**
     * 0.根据地址名称得到两个地址间的距离
     *
     * @param start 起始位置
     * @param start 结束位置
     * @return 两个地址间的距离
     */
    public static long getDistanceByAddress(String start, String end) {
        String startLonLat = getLonLat(start);
        String endLonLat = getLonLat(end);
        long dis = getApiDistance(startLonLat, endLonLat);
        return dis;
    }

    /**
     * 1.地址转换为经纬度
     *
     * @param address 地址
     * @return 经纬度
     */
    public static String getLonLat(String address) {
        // 返回输入地址address的经纬度信息, 格式是 经度,纬度
        String queryUrl = "https://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=JSON&key=" + GOULD_KEY;
        String queryResult = getResponse(queryUrl);
        JSONObject job = JSONObject.parseObject(queryResult);
        if (Objects.isNull(job.get(GEOCODES)) || job.get(GEOCODES).toString().equals(ARRY)) {
            return Global.Str_1;
        }
        System.out.println(job.get(GEOCODES).toString());
        JSONArray sddressArr = JSON.parseArray(job.get(GEOCODES).toString());
        JSONObject c = JSON.parseObject(sddressArr.get(0).toString());
        String DZ = c.get(LOCATION).toString();
        return DZ;
    }

    /**
     * 将经纬度getLng， getLat 通过getAMapByLngAndLat方法转换地址
     *
     * @param getLng 经度
     * @param getLat 纬度
     * @return 地址名称
     * @throws Exception
     */
    public String getAMapByLngAndLat(String getLng, String getLat) throws Exception {
        String url;
        try {
            url = "http://restapi.amap.com/v3/geocode/regeo?output=JSON&key=" + GOULD_KEY + "&location=" + getLat + "," + getLng + "&radius=1000&extensions=all";
            System.out.println(getLat);
            System.out.println(url);
            String queryResult = getResponse(url);
            if (queryResult == null) {
                return Global.Str_1;
            }
            // 将获取结果转为json 数据
            JSONObject obj = JSONObject.parseObject(queryResult);
            if (obj.get(STATUS).toString().equals(STR_1)) {
                // 如果没有返回-1
                JSONObject regeocode = obj.getJSONObject(REGEOCODES);
                if (regeocode.size() > 0) {
                    // 在regeocode中拿到 formatted_address 具体位置
                    String formatted = regeocode.get(FROMATTED_ADDRESS).toString();
                    return formatted;
                } else {
                    System.out.println("未找到相匹配的地址！");
                    return Global.Str_1;
                }
            } else {
                System.out.println("请求错误！");
                return Global.Str_1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1";
    }

    /**
     * 2.根据两个定位点的经纬度算出两点间的距离
     * 高德api
     *
     * @return 两个定位点之间的距离
     */
    public static long getApiDistance(String startLonLat, String endLonLat) {
        // 返回起始地startAddr与目的地endAddr之间的距离，单位：米
        Long result = new Long(0);
        String queryUrl = "http://restapi.amap.com/v3/distance?key=" + GOULD_KEY + "&origins=" + startLonLat + "&destination=" + endLonLat;

        String queryResult = getResponse(queryUrl);
        JSONObject job = JSONObject.parseObject(queryResult);
        JSONArray ja = job.getJSONArray("results");
        JSONObject jobO = JSONObject.parseObject(ja.getString(0));
        result = Long.parseLong(jobO.get("distance").toString());
        return result;
    }

    /**
     * 3.发送请求
     *
     * @param serverUrl 请求地址
     */
    private static String getResponse(String serverUrl) {
        /* 用JAVA发起http请求，并返回json格式的结果 */
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 本地计算二个经纬度的距离 jdk的Math
     * Lng 经度
     *
     * @param lonLat
     * @param lonLat1
     * @return
     */
    public static double getDistance(String lonLat, String lonLat1) {
        String[] end = lonLat1.split(",");
        String[] start = lonLat.split(",");
        //end[1],start[1],end[0],start[0]
        //double lat1,lat2, lon1,  lon2;
        if (StringUtils.isEmpty(end[1]) || StringUtils.isEmpty(start[1]) || StringUtils.isEmpty(end[0]) || StringUtils.isEmpty(start[0])) {
            return -1;
        }
        double lat11 = Double.parseDouble(end[1]);
        double lat12 = Double.parseDouble(start[1]);
        double lon11 = Double.parseDouble(end[0]);
        double lon12 = Double.parseDouble(start[0]);
        double latDistance = Math.toRadians(lat12 - lat11);
        double lonDistance = Math.toRadians(lon12 - lon11);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat11)) * Math.cos(Math.toRadians(lat12))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        /* 单位转换成米 */
        double distance = R * c * Global.INT_1000;
        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }

//	public static void main(String[] args) {
//		String s="重庆市渝北区天宫殿街道泰山大道东段106号";
//		String lonLat = GouldUtil.getLonLat(s);
//		System.out.println(lonLat);
//	}

}