package com.architect.mapper;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ItemsCommentsMapperCustom {
    void saveComments(Map<String, Object> map);
}