package com.atguigu.scw.project.service;

import com.atguigu.scw.project.vo.request.ProjectRedisStorageVo;

/**
 * @author erdong
 * @create 2019-10-08 21:19
 */
public interface ProjectService {

    void saveProject(ProjectRedisStorageVo bigVo);
}
