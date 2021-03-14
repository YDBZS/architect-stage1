package org.architect.service.impl;

import com.architect.mapper.*;
import com.architect.pojo.*;
import com.architect.pojo.vo.CommentLevelCountVO;
import com.architect.pojo.vo.ItemCommentVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.architect.enums.CommentLevel;
import org.architect.service.ItemService;
import org.architect.util.DesensitizationUtil;
import org.architect.util.PagedGridResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author 多宝
 * @since 2021/3/14 11:10
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemsMapper itemsMapper;
    @Resource
    private ItemsImgMapper itemsImgMapper;
    @Resource
    private ItemsSpecMapper itemsSpecMapper;
    @Resource
    private ItemsParamMapper itemsParamMapper;
    @Resource
    private ItemsCommentsMapper itemsCommentsMapper;
    @Resource
    private ItemsMapperCustom itemsMapperCustom;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> queryItemImageList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam queryItemParamList(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Override
    public CommentLevelCountVO queryCommentCounts(String itemId) {

        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountVO countVO = new CommentLevelCountVO();
        countVO.setTotalCounts(totalCounts);
        countVO.setGoodCounts(goodCounts);
        countVO.setNormalCounts(normalCounts);
        countVO.setBadCounts(badCounts);
        return countVO;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (null != level) {
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryPagedComments(String itemId,
                                              Integer level,
                                              Integer page,
                                              Integer pageSize) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("itemId", itemId);
        map.put("level", level);

        // mybatis-pagehelper
        PageHelper.startPage(page, pageSize);

        List<ItemCommentVO> list = itemsMapperCustom.queryItemComments(map);
        list.forEach(item -> {
            item.setNickname(DesensitizationUtil.commonDisplay(item.getNickname()));
        });
        return setterPagedGrid(list, page);
    }

    private PagedGridResult setterPagedGrid(List<?> list, int page) {
        PageInfo<?> pagelist = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pagelist.getPages());
        grid.setRecords(pagelist.getTotal());
        return grid;
    }
}
