package org.architect.service.impl;

import com.architect.mapper.CategoryMapper;
import com.architect.mapper.CategoryMapperCustom;
import com.architect.pojo.Category;
import com.architect.pojo.vo.CategoryVO;
import com.architect.pojo.vo.NewItemsVO;
import com.google.common.collect.Maps;
import org.architect.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author 多宝
 * @since 2021/3/14 9:15
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private CategoryMapperCustom categoryMapperCustom;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);

        return categoryMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapperCustom.getSubCatList(rootCatId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("rootCatId", rootCatId);
        return categoryMapperCustom.getSixNewItemsLazy(map);
    }
}
