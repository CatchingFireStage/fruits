package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.fruits.fruits.mapper.po.SpuCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Service
public class SpuCategoryAdminModuleService {

    @Autowired
    private SpuCategoryService spuCategoryService;

    /**
     * 搜索
     */
    public List<SpuCategory> getSpuCategories(String keyword) throws IOException {

        LambdaQueryChainWrapper<SpuCategory> queryWrapper = spuCategoryService.lambdaQuery();

        if (keyword != null && !keyword.trim().equals("")) {
            IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(keyword), true);
            Lexeme lex;
            while ((lex = ikSegmenter.next()) != null) {
                queryWrapper.like(SpuCategory::getName, lex.getLexemeText()).or();
            }
            queryWrapper.like(SpuCategory::getName, keyword.trim());
        }

        queryWrapper.last("limit 100");

        return queryWrapper.list();
    }


    /**
     * 列表
     */
    public IPage<SpuCategory> getSpuCategories(int p, int pageSize, String keyword) {

        LambdaQueryChainWrapper<SpuCategory> queryWrapper = spuCategoryService.lambdaQuery();

        if (keyword != null && !keyword.equals("")) {
            queryWrapper.like(SpuCategory::getName, keyword);
        }

        queryWrapper.orderByDesc(SpuCategory::getId);

        return spuCategoryService.page(new Page<>(p, pageSize), queryWrapper);
    }
}
