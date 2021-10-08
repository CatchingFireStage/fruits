package me.fruits.fruits.service.spu;


import me.fruits.fruits.mapper.po.*;
import me.fruits.fruits.service.spu.dto.SpecificationSpuDTO;
import me.fruits.fruits.service.spu.dto.SpecificationValueDTO;
import me.fruits.fruits.service.spu.dto.SpuCategoryDTO;
import me.fruits.fruits.service.spu.dto.SpuDTO;
import me.fruits.fruits.service.upload.UploadService;
import me.fruits.fruits.utils.MoneyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 包装的dto的前后台公共service
 */
@Service
public class WrapperDTOService {


    @Autowired
    private SpecificationValueService specificationValueService;


    @Autowired
    private SpuCategoryService spuCategoryService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private SpecificationSpuService specificationSpuService;

    @Autowired
    private SpecificationService specificationService;


    /**
     * 包装spu
     */
    public List<SpuDTO> wrapperSPUs(List<Spu> spus) {

        if (spus == null || spus.size() == 0) {
            return new ArrayList<>();
        }


        //分类填充
        Set<Long> categoryIds = spus.stream().map(Spu::getCategoryId).collect(Collectors.toSet());
        Map<Long, SpuCategory> categoryMapKeyIsId = spuCategoryService.getSpuCategories(categoryIds).stream().collect(Collectors.toMap(SpuCategory::getId, spuCategory -> spuCategory));


        //规格填充

        //获取关联关系
        Set<Long> ids = spus.stream().map(Spu::getId).collect(Collectors.toSet());
        List<SpecificationSpu> specificationSpuList = specificationSpuService.getSpecificationSpuBySpuId(ids);
        Map<Long, List<SpecificationSpu>> specificationSpuMapKeyIsSpuId = specificationSpuList.stream().collect(Collectors.groupingBy(SpecificationSpu::getSpuId));

        Map<Long, List<SpecificationSpuDTO>> SpecificationDTOMapKeyIsSpuId = new HashMap<>();

        specificationSpuMapKeyIsSpuId.forEach((spuId, SpecificationSpuList) -> {

            try {
                //获取所关联的规格
                Set<Long> specificationIds = specificationSpuList.stream().map(SpecificationSpu::getSpecificationId).collect(Collectors.toSet());
                List<Specification> specifications = specificationService.getSpecifications(specificationIds);
                //包装成dto
                List<SpecificationSpuDTO> specificationDTOS = wrapperSpecifications(specifications, spuId);


                SpecificationDTOMapKeyIsSpuId.put(spuId, specificationDTOS);
            } catch (RuntimeException ignored) {

            }
        });


        List<SpuDTO> response = new ArrayList<>();

        //构建每一项
        spus.forEach(spu -> {
            SpuDTO item = new SpuDTO();
            item.setId(spu.getId());
            item.setName(spu.getName());
            item.setImage(uploadService.imageVo(spu.getImage()));
            item.setIsInventory(spu.getIsInventory());
            item.setMoney(MoneyUtils.fenChangeYuan(spu.getMoney()));

            //分类dto
            item.setCategory(null);
            if (categoryMapKeyIsId.containsKey(spu.getCategoryId())) {

                SpuCategoryDTO spuCategoryDTO = new SpuCategoryDTO();
                spuCategoryDTO.setId(categoryMapKeyIsId.get(spu.getCategoryId()).getId());
                spuCategoryDTO.setName(categoryMapKeyIsId.get(spu.getCategoryId()).getName());

                item.setCategory(spuCategoryDTO);
            }

            //规格dto
            item.setSpecificationSPUs(SpecificationDTOMapKeyIsSpuId.getOrDefault(spu.getId(), null));

            response.add(item);
        });


        return response;
    }


    private List<SpecificationSpuDTO> wrapperSpecifications(List<Specification> specifications, long spuId) {

        List<SpecificationSpuDTO> specificationSpuDTOS = wrapperSpecifications(specifications);

        //获取spu必选的规格
        List<SpecificationSpu> specificationSpuRequiredList = specificationSpuService.getSpecificationSpuRequiredBySpuId(spuId);

        Set<Long> requiredSpecificationSpu = specificationSpuRequiredList.stream().map(SpecificationSpu::getSpecificationId).collect(Collectors.toSet());

        specificationSpuDTOS.forEach(specificationSpuDTO -> {

            //设置规格是否为必选的
            specificationSpuDTO.setRequired(requiredSpecificationSpu.contains(specificationSpuDTO.getId()));
        });

        return specificationSpuDTOS;
    }

    /**
     * 包装规格
     */
    public List<SpecificationSpuDTO> wrapperSpecifications(List<Specification> specifications) {

        if (specifications == null || specifications.size() == 0) {
            return new ArrayList<>();
        }

        //填充规格值
        Set<Long> ids = specifications.stream().map(Specification::getId).collect(Collectors.toSet());
        Map<Long, List<SpecificationValue>> valueMapKeyIsId = specificationValueService.getSpecificationValues(ids).stream().collect(Collectors.groupingBy(SpecificationValue::getSpecificationId));


        List<SpecificationSpuDTO> response = new ArrayList<>();

        specifications.forEach(specification -> {

            SpecificationSpuDTO item = new SpecificationSpuDTO();
            item.setId(specification.getId());
            item.setName(specification.getName());
            item.setRequired(null);

            //规格值
            item.setValues(null);
            if (valueMapKeyIsId.containsKey(specification.getId())) {
                List<SpecificationValueDTO> values = new ArrayList<>();

                valueMapKeyIsId.get(specification.getId()).forEach(value -> {

                    SpecificationValueDTO specificationValueDTO = new SpecificationValueDTO();
                    specificationValueDTO.setId(value.getId());
                    specificationValueDTO.setValue(value.getValue());
                    specificationValueDTO.setMoney(MoneyUtils.fenChangeYuan(value.getMoney()));

                    values.add(specificationValueDTO);

                });

                item.setValues(values);
            }


            response.add(item);

        });

        return response;
    }
}
