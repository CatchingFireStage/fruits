package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.SpuCategoryMapper;
import me.fruits.fruits.mapper.SpuMapper;
import me.fruits.fruits.mapper.po.Spu;
import me.fruits.fruits.mapper.po.SpuCategory;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpuCategoryAdminModuleService extends SpuCategoryService {
    @Autowired
    private SpuCategoryMapper spuCategoryMapper;

    @Autowired
    private SpuMapper spuMapper;


    /**
     * 搜索
     */
    public List<SpuCategory> getSpuCategories(String keyword) throws IOException {

        QueryWrapper<SpuCategory> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().equals("")) {
            IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(keyword), true);
            Lexeme lex;
            while ((lex = ikSegmenter.next()) != null) {
                queryWrapper.like("name", lex.getLexemeText()).or();
            }
            queryWrapper.like("name", keyword.trim());
        }

        queryWrapper.last("limit 100");


        return this.spuCategoryMapper.selectList(queryWrapper);
    }


    /**
     * 列表
     */
    public IPage<SpuCategory> getSpuCategories(int p, int pageSize, String keyword) {
        QueryWrapper<SpuCategory> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like("name", keyword);
        }

        queryWrapper.orderByDesc("id");

        return this.spuCategoryMapper.selectPage(new Page<>(p, pageSize), queryWrapper);
    }


    /**
     * 添加
     */
    public void add(SpuCategory spuCategory) {
        super.add(spuCategory);
    }

    /**
     * 更新
     */
    public void update(long id, SpuCategory spuCategory) {

        UpdateWrapper<SpuCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", spuCategory.getName()).set("update_time", LocalDateTime.now());

        this.spuCategoryMapper.update(null, updateWrapper);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(long id) throws FruitsException {

        QueryWrapper<Spu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id",id);
        if(this.spuMapper.selectCount(queryWrapper) > 0){
            throw new FruitsException("删除分类下所有商品才能删除分类");
        }

        this.spuCategoryMapper.deleteById(id);
    }
}
