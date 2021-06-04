package org.architect.service.impl;

import com.architect.mapper.*;
import com.architect.pojo.*;
import com.architect.pojo.vo.CommentLevelCountVO;
import com.architect.pojo.vo.ItemCommentVO;
import com.architect.pojo.vo.SearchItemsVO;
import com.architect.pojo.vo.ShopcartVO;
import com.architect.util.PageUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.architect.enums.CommentLevel;
import org.architect.enums.YesOrNo;
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
        return PageUtil.setterPagedGrid(list, page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("keywords", keywords);
        map.put("sort", sort);
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> itemsVOList = itemsMapperCustom.searchItems(map);
        return PageUtil.setterPagedGrid(itemsVOList, page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("catId", catId);
        map.put("sort", sort);
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> itemsVOList = itemsMapperCustom.searchItemsByThirdCat(map);
        return PageUtil.setterPagedGrid(itemsVOList, page);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String[] ids = specIds.split(",");
        List<String> specIdsList = Lists.newArrayList(ids);
        return itemsMapperCustom.queryItemBySpecIds(specIdsList);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg img = itemsImgMapper.selectOne(itemsImg);
        return null != img ? img.getUrl() : "";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseItemSpecStock(String specId, int buyCounts) {
        // 减库存：商品数据超卖解决
        // synchronized 不推荐使用，集群下无用，性能低下
        // 锁数据库：不推荐，导致数据库性能低下
        // 集群下可以使用分布式锁   zookeeper redis

        // 1 查询库存

        // 2、判断库存，是否能减少到零以下

        int result = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足！");
        }
    }
}
