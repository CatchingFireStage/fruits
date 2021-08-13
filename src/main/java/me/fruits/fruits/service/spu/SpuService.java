package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.upload.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 公共的服务
 */
abstract public class SpuService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private UploadService uploadService;

    /**
     * 添加一个spu
     */
    public void add(Spu spu) {

        spu.setCreateTime(LocalDateTime.now());
        spu.setUpdateTime(LocalDateTime.now());
        this.spuMapper.insert(spu);
    }


    /**
     * 删除一个spu
     */
    @Transactional
    public void delete(long id){

        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);

        Spu spu = this.spuMapper.selectOne(queryWrapper);

        //删除文件
        this.uploadService.delete(spu.getImage());

        this.spuMapper.deleteById(spu.getId());
    }
}
