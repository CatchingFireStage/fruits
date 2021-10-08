package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.upload.UploadService;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 公共的服务
 */
@Service
public class SpuService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private UploadService uploadService;


    public Spu getSpu(long id) {
        return this.spuMapper.selectById(id);
    }

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
    public void delete(long id) {

        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);

        Spu spu = this.spuMapper.selectOne(queryWrapper);

        //删除文件
        this.uploadService.delete(spu.getImage());

        this.spuMapper.deleteById(spu.getId());
    }



    /**
     * 更新spu的分类、是否有有货
     */
    public void update(long id, Spu spu) {
        UpdateWrapper<Spu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("category_id", spu.getCategoryId())
                .set("is_inventory", spu.getIsInventory())
                .set("money", spu.getMoney())
                .set("update_time", LocalDateTime.now());
        this.spuMapper.update(null, updateWrapper);
    }

    /**
     * 更变图片
     */
    @Transactional
    public void update(long id, MultipartFile file) throws IOException, FruitsException {
        Spu spu = this.spuMapper.selectById(id);

        //删除图片
        this.uploadService.delete(spu.getImage());

        //上传新的
        String newImage = this.uploadService.uploadBySpu(file);

        UpdateWrapper<Spu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("image", newImage);
        this.spuMapper.update(null, updateWrapper);

    }
}
