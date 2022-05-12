package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.service.upload.UploadService;
import me.fruits.fruits.utils.FruitsException;
import me.fruits.fruits.utils.MoneyUtils;
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
public class SpuService extends ServiceImpl<SpuMapper, Spu> {

    @Autowired
    private UploadService uploadService;


    public Spu getSpu(long id) {
        return getById(id);
    }


    /**
     * @param name        商品名
     * @param categoryId  商品分类id
     * @param money       金额,单位元
     * @param isInventory 0没有货，1有货
     * @param file        商品图片
     */
    public void add(String name, long categoryId, String money, Integer isInventory, MultipartFile file) throws IOException, FruitsException {
        Spu spu = new Spu();
        spu.setName(name);
        spu.setCategoryId(categoryId);
        spu.setMoney(MoneyUtils.yuanChangeFen(money));
        spu.setIsInventory(isInventory);
        //图片处理
        String path = uploadService.uploadBySpu(file);
        spu.setImage(path);

        spu.setCreateTime(LocalDateTime.now());
        spu.setUpdateTime(LocalDateTime.now());

        save(spu);
    }


    /**
     * 删除一个spu
     */
    @Transactional
    public void delete(long id) {

        Spu spu = lambdaQuery().eq(Spu::getId, id).one();

        //删除文件
        this.uploadService.delete(spu.getImage());

        this.removeById(spu.getId());
    }


    /**
     * 更新spu的元数据和图片
     *
     * @param id          唯一标识
     * @param name        spu名字
     * @param categoryId  分类
     * @param money       金额
     * @param isInventory 是否有货
     * @param file        新的图片
     */
    @Transactional
    public void update(long id, String name, long categoryId, String money, int isInventory, MultipartFile file) throws IOException, FruitsException {
        //更新元数据
        update(id, name, categoryId, money, isInventory);

        if (file != null && !file.isEmpty()) {
            //更新图片
            update(id, file);
        }
    }

    /**
     * 更新spu的分类、是否有有货
     */
    public void update(long id, String name, long categoryId, String money, int isInventory) {

        lambdaUpdate().eq(Spu::getId, id)
                .set(Spu::getName, name)
                .set(Spu::getCategoryId, categoryId)
                .set(Spu::getMoney, MoneyUtils.yuanChangeFen(money))
                .set(Spu::getIsInventory, isInventory)
                .set(Spu::getUpdateTime, LocalDateTime.now())
                .update();
    }

    /**
     * 更变图片
     */
    @Transactional
    public void update(long id, MultipartFile file) throws IOException, FruitsException {
        Spu spu = getById(id);

        //删除图片
        this.uploadService.delete(spu.getImage());

        //上传新的
        String newImage = this.uploadService.uploadBySpu(file);

        lambdaUpdate().eq(Spu::getId, id)
                .set(Spu::getImage, newImage)
                .update();

    }
}
