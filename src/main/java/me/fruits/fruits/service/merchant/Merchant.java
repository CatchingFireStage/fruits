package me.fruits.fruits.service.merchant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class Merchant {

    //营业开始时间
    @Value("${fruits.merchant.start-time}")
    private LocalDateTime startTime;

    //营业结束时间
    @Value("${fruits.merchant.end-time}")
    private LocalDateTime endTime;

    //是否24小时
    @Value("${fruits.merchant.is-24-hours}")
    private Boolean is24Hours;

    //是否关门休息
    @Value("${fruits.merchant.is-close}")
    private Boolean isClose;


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
}
