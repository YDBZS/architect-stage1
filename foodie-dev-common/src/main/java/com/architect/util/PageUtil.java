package com.architect.util;

import com.github.pagehelper.PageInfo;
import org.architect.util.PagedGridResult;

import java.util.List;

/**
 * 分页工具类
 *
 * @author 多宝
 * @since 2021/6/3 21:20
 */
public class PageUtil {

    public static PagedGridResult setterPagedGrid(List<?> list, int page) {
        PageInfo<?> pagelist = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pagelist.getPages());
        grid.setRecords(pagelist.getTotal());
        return grid;
    }

}
