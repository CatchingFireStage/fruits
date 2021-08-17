package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.enums.IsInventoryEnum;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.upload.UploadService;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SpuAdminModuleService extends SpuService {

    @Autowired
    private SpuMapper spuMapper;


    @Autowired
    private UploadService uploadService;

    /**
     * spu列表
     */
    public IPage<Spu> getSPUs(int p, int pageSize, String keyword) {

        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>();

        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like("name", keyword);
        }

        queryWrapper.orderByDesc("id");

        return this.spuMapper.selectPage(new Page<>(p, pageSize), queryWrapper);
    }
    
    /**
     * 更新spu的分类、是否有有货
     */
    public void update(long id, Spu spu) {
        UpdateWrapper<Spu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("category_id", spu.getCategoryId())
                .set("is_inventory", spu.getIsInventory())
                .set("update_time", LocalDateTime.now());
        this.spuMapper.update(null, updateWrapper);
    }

    /**
     * 改变是否有货
     */
    public void update(long id, IsInventoryEnum isInventoryEnum) {

        UpdateWrapper<Spu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("is_inventory", isInventoryEnum.getValue());
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
        updateWrapper.set("image",newImage);
        this.spuMapper.update(null, updateWrapper);

    }
}
