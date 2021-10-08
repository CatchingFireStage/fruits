package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class SpuAdminModuleService {

    @Autowired
    private SpuMapper spuMapper;


    @Autowired
    private SpuService spuService;

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

    public Spu getSPU(long id){
        return this.spuMapper.selectById(id);
    }


    /**
     * 添加一个spu
     */
    public void add(Spu spu) {

        this.spuService.add(spu);
    }

    /**
     * 删除一个spu
     */
    public void delete(long id) {
        this.spuService.delete(id);
    }

    /**
     * 更新spu的元数据和图片
     * @param id 唯一标识
     * @param spu spu元数据
     * @param file 新的图片
     */
    @Transactional
    public void update(long id,Spu spu,MultipartFile file) throws IOException, FruitsException{
        //更新元数据
        this.spuService.update(id, spu);

        if(!file.isEmpty()){
            //更新图片
            this.spuService.update(id, file);
        }
    }
}
