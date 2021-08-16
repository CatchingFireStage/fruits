package me.fruits.fruits.service.spu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.mapper.SpecificationMapper;
import me.fruits.fruits.mapper.po.Specification;
import me.fruits.fruits.utils.FruitsException;
import me.fruits.fruits.utils.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Service
@Slf4j
public class SpecificationAdminModuleService extends SpecificationService {
    @Autowired
    private SpecificationMapper specificationMapper;


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

    /**
     * 搜索用
     *
     * @param keyword 搜索词
     */
    public List<Specification> getSpecifications(String keyword) throws IOException {
        QueryWrapper<Specification> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().equals("")) {
            IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(keyword), true);
            Lexeme lex;
            while ((lex = ikSegmenter.next()) != null) {
                queryWrapper.like("name", lex.getLexemeText()).or();
            }
            queryWrapper.like("name", keyword.trim());
        }

        queryWrapper.last("limit 100");


        return this.specificationMapper.selectList(queryWrapper);
    }


    public void update(long id, Specification specification) throws FruitsException {

        UpdateWrapper<Specification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("name", specification.getName());

        try {
            this.specificationMapper.update(null, updateWrapper);
        } catch (DuplicateKeyException e) {
            throw new FruitsException("规格名已存在");
        }
    }
}
