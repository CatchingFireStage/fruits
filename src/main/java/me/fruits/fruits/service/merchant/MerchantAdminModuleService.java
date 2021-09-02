package me.fruits.fruits.service.merchant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MerchantAdminModuleService {

    @Autowired
    private MerchantService merchantService;


    public MerchantService.MerchantDTO getMerchant() {

        return merchantService.getMerchant();
    }

    public void setStartTime(LocalDateTime startTime) {
        merchantService.setStartTime(startTime);
    }

    public void setEndTime(LocalDateTime endTime) {

        merchantService.setEndTime(endTime);
    }

    public void setIs24Hours(boolean is24Hours) {

        merchantService.setIs24Hours(is24Hours);
    }

    public void setIsClose(boolean isClose) {

        merchantService.setIsClose(isClose);
    }
}
