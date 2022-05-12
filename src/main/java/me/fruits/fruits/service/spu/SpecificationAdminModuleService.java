package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.po.Specification;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

@Service
@Slf4j
public class SpecificationAdminModuleService {
    @Autowired
    private SpecificationService specificationService;


    /**
     * 列表
     */
    public IPage<Specification> getSpecifications(String keyword, PageVo pageVo) {

        LambdaQueryChainWrapper<Specification> queryWrapper = specificationService.lambdaQuery();

        if (keyword != null && !keyword.trim().equals("")) {
            try {
                IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(keyword), true);
                Lexeme lex;
                while ((lex = ikSegmenter.next()) != null) {
                    queryWrapper.like(Specification::getName, lex.getLexemeText()).or();
                }
                queryWrapper.like(Specification::getName, keyword.trim());
            } catch (IOException ignored) {

            }
        }


        queryWrapper.orderByDesc(Specification::getId);

        return queryWrapper.page(new Page<>(pageVo.getP(), pageVo.getPageSize()));
    }


}
