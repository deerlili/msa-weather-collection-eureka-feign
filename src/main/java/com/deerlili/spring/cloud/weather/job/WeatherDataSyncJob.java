package com.deerlili.spring.cloud.weather.job;

import com.deerlili.spring.cloud.weather.entity.City;
import com.deerlili.spring.cloud.weather.service.CityClient;
import com.deerlili.spring.cloud.weather.service.WeatherDataCollectionService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author deerlili
 * @Classname WeatherDataSyncJob
 * @Description TODO
 * @Date 2020/7/8 20:22
 * @Version V1.0
 */
public class WeatherDataSyncJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(WeatherDataSyncJob.class);

    @Autowired
    private WeatherDataCollectionService weatherDataCollectionService;

    @Autowired
    private CityClient cityClient;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Weather Data Sync Job. Start!");
        // 获取城市ID列表
        List<City> cityList = null;
        try {
            // 调用城市API微服务
            cityList = cityClient.cityList();
        } catch (Exception e) {
            //e.printStackTrace();
            logger.info("Exception！", e);
        }
        // 遍历城市ID天气
        for (City city:cityList) {
            String cityId = city.getCityId();
            logger.info("Weather Data Sync Job cityId:" + cityId);
            weatherDataCollectionService.syncDataByCityId(cityId);
        }
        logger.info("Weather Data Sync Job. End!");
    }
}
