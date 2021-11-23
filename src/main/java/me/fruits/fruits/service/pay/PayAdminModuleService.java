package me.fruits.fruits.service.pay;

import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.PayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayAdminModuleService {


    @Autowired
    private PayMapper payMapper;



}
