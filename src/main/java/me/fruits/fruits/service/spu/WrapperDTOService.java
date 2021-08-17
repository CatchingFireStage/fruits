package me.fruits.fruits.service.spu;


import me.fruits.fruits.mapper.po.Specification;
import me.fruits.fruits.mapper.po.SpecificationValue;
import me.fruits.fruits.service.spu.dto.SpecificationDTO;
import me.fruits.fruits.service.spu.dto.SpecificationValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 包装的dto的service
 */
@Service
public class WrapperDTOService {


    @Autowired
    private SpecificationValueService specificationValueService;

    /**
     * 包装一下
     * @param specifications
     * @return
     */
    public List<SpecificationDTO> wrapperSpecificationValues(List<Specification> specifications) {

        if (specifications == null || specifications.size() == 0) {
            return new ArrayList<>();
        }

        //填充规格值
        Set<Long> ids = specifications.stream().map(Specification::getId).collect(Collectors.toSet());
        Map<Long, List<SpecificationValue>> valueMapKeyIsId = specificationValueService.getSpecificationValues(ids).stream().collect(Collectors.groupingBy(SpecificationValue::getSpecificationId));


        List<SpecificationDTO> response = new ArrayList<>();

        specifications.forEach(specification -> {

            SpecificationDTO item = new SpecificationDTO();
            item.setId(specification.getId());
            item.setName(specification.getName());

            //规格值
            item.setValues(null);
            if (valueMapKeyIsId.containsKey(specification.getId())) {
                List<SpecificationValueDTO> values = new ArrayList<>();

                valueMapKeyIsId.get(specification.getId()).forEach(value -> {

                    SpecificationValueDTO specificationValueDTO = new SpecificationValueDTO();
                    specificationValueDTO.setId(value.getId());
                    specificationValueDTO.setValue(value.getValue());

                    values.add(specificationValueDTO);

                });

                item.setValues(values);
            }


            response.add(item);

        });

        return response;
    }
}
