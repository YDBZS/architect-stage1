package org.architect.service;

import com.architect.pojo.Stu;

/**
 * 学生信息数据库测试
 *
 * @author 多宝
 * @since 2021/3/12 22:52
 */
public interface StuService {

    public Stu getStuInfo(int id);

    public void saveStu();

    public void updateStu(int id);

    public void deleteStu(int id);
}
