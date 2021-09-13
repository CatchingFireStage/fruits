package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.SpecificationMapper;
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
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationService specificationService;


    /**
     * 列表
     */
    public IPage<Specification> getSpecifications(String keyword, PageVo pageVo) {
        QueryWrapper<Specification> queryWrapper = new QueryWrapper<>();

        if (keyword != null && !keyword.trim().equals("")) {
            try {
                IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(keyword), true);
                Lexeme lex;
                while ((lex = ikSegmenter.next()) != null) {
                    queryWrapper.like("name", lex.getLexemeText()).or();
                }
                queryWrapper.like("name", keyword.trim());
            } catch (IOException ignored) {

            }
        }


        queryWrapper.orderByDesc("id");

        return this.specificationMapper.selectPage(new Page<>(pageVo.getP(), pageVo.getPageSize()), queryWrapper);
    }

    public void add(Specification specification) {

        this.specificationService.add(specification);
    }


    public void update(long id, Specification specification) {

        this.specificationService.update(id, specification.getName());
    }


}
