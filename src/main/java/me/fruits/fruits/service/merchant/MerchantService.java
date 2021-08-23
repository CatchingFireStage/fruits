package me.fruits.fruits.service.merchant;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class MerchantService {

    //营业开始时间
    @Value("${fruits.merchant.start-time}")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    //营业结束时间
    @Value("${fruits.merchant.end-time}")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    //是否24小时
    @Value("${fruits.merchant.is-24-hours}")
    private Boolean is24Hours;

    //是否关门休息
    @Value("${fruits.merchant.is-close}")
    private Boolean isClose;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");


    public synchronized void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public synchronized void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public synchronized void setIs24Hours(boolean is24Hours) {
        this.is24Hours = is24Hours;
    }

    public synchronized void setIsClose(boolean isClose) {
        this.isClose = isClose;
    }


    /**
     * 是否营业中
     */
    public boolean isOpen() {

        if (isClose) {
            //关门休息
            return true;
        }


        if (is24Hours) {
            //24小时营业
            return true;
        } else {
            //不是24小时营业
            LocalDateTime now = LocalDateTime.now();
            if (startTime.getHour() <= now.getHour() && now.getHour() <= endTime.getHour()) {


                if (startTime.getHour() == now.getHour() && startTime.getMinute() > now.getMinute()) {
                    //没有开始营业
                    return false;
                }

                if (endTime.getHour() == now.getHour() && endTime.getMinute() < now.getMinute()) {
                    //营业结束时间到了
                    return false;
                }

                //营业中
                return true;

            }
        }

        return false;
    }


    public MerchantDTO getMerchant() {


        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setStartTime(this.startTime.format(dateTimeFormatter));
        merchantDTO.setEndTime(this.endTime.format(dateTimeFormatter));
        merchantDTO.setIs24Hours(this.is24Hours);
        merchantDTO.setIsClose(this.isClose);

        return merchantDTO;
    }


    @ApiModel("商家信息")
    @Data
    public static class MerchantDTO {
        //营业开始时间
        private String startTime;

        //营业结束时间
        private String endTime;

        //是否24小时
        private Boolean is24Hours;

        //是否关门休息
        private Boolean isClose;
    }
}
