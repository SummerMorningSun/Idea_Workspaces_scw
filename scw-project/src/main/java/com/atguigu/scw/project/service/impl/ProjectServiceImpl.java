package com.atguigu.scw.project.service.impl;

import com.atguigu.scw.project.bean.*;
import com.atguigu.scw.project.mapper.*;
import com.atguigu.scw.project.service.ProjectService;
import com.atguigu.scw.project.vo.request.ProjectRedisStorageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author erdong
 * @create 2019-10-08 21:21
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    //将bigVo拆分存到数据库的表中：
    /**
     * <table tableName="t_project_images"></table>
     * <table tableName="t_project"></table>
     * <table tableName="t_project_tag"></table>
     * <table tableName="t_project_type"></table>
     * <table tableName="t_return"></table>
     * <table tableName="t_project_initiator"></table>
     */
    @Autowired
    TProjectMapper projectMapper;
    @Autowired
    TProjectImagesMapper projectImagesMapper;
    @Autowired
    TProjectTagMapper projectTagMapper;
    @Autowired
    TProjectTypeMapper projectTypeMapper;
    @Autowired
    TProjectInitiatorMapper projectInitiatorMapper;
    @Autowired
    TReturnMapper returnMapper;

    @Override
    public void saveProject(ProjectRedisStorageVo bigVo) {
        //1、先保存project并得到project的id  t_project
        //bigVo的属性名和javabean的属性名都是一样的
        TProject tProject = new TProject();
        BeanUtils.copyProperties(bigVo, tProject);
        //设置需要手动初始化的值
        tProject.setMoney((long) bigVo.getMoney());
        tProject.setStatus("0");
        tProject.setFollower(0);
        tProject.setCreatedate(com.atguigu.scw.common.utils.AppDateUtils.getFormatTime());
        projectMapper.insertSelective(tProject);

        Integer projectId = tProject.getId();//获取自增的主键id值
        //2、存图片地址t_project_images
        //创建保存图片信息的集合
        List<TProjectImages> imgs = new ArrayList<TProjectImages>();
        TProjectImages headerImg = new TProjectImages();
        //保存头图：
        headerImg.setImgtype((byte) 0);
        headerImg.setProjectid(projectId);
        headerImg.setImgurl(bigVo.getHeaderImage());
        imgs.add(headerImg);
        log.info("projectid:" + projectId);
        //projectImagesMapper.insertSelective(tProjectImages);
        //保存详情图:  批量插入 insert into t_project_images()  values() , () , ();
        for (String imgUrl : bigVo.getDetailsImage()) {
            TProjectImages detailsImg = new TProjectImages();
            detailsImg.setImgtype((byte) 1);
            detailsImg.setProjectid(projectId);
            detailsImg.setImgurl(imgUrl);
            imgs.add(detailsImg);
        }
        //将图片集合存到数据库中
        projectImagesMapper.insertImgs(imgs);

        //3、存项目tags：t_project_tag
        //将项目的tag信息转为TProjectTag对象的集合
        List<Integer> tagids = bigVo.getTagids();
        for (Integer tagid : tagids) {
            TProjectTag tProjectTag = new TProjectTag();
            tProjectTag.setProjectid(projectId);
            tProjectTag.setTagid(tagid);
            projectTagMapper.insertSelective(tProjectTag);
        }
        //4、存项目type：t_project_type
        List<Integer> typeids = bigVo.getTypeids();
        for (Integer typeid : typeids) {
            TProjectType tProjectType = new TProjectType();
            tProjectType.setProjectid(projectId);
            tProjectType.setTypeid(typeid);
            projectTypeMapper.insertSelective(tProjectType);
        }
        //5、存项目发起人信息：t_project_initiator
        TProjectInitiator projectInitiator = bigVo.getProjectInitiator();
        //！！一定要将项目发起人所属的项目id设置给发起人
        projectInitiator.setProjectid(projectId);
        projectInitiatorMapper.insertSelective(projectInitiator);
        //6、存项目回报：t_return
        List<TReturn> projectReturns = bigVo.getProjectReturns();
        for (TReturn tReturn : projectReturns) {
            tReturn.setProjectid(projectId);
            returnMapper.insertSelective(tReturn);
        }
    }
}
