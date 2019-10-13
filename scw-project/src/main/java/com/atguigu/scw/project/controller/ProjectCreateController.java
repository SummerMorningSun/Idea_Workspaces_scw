package com.atguigu.scw.project.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.scw.common.consts.AppConsts;
import com.atguigu.scw.common.templates.OssTemplate;
import com.atguigu.scw.common.utils.AppResponse;
import com.atguigu.scw.common.utils.ScwUtils;
import com.atguigu.scw.project.bean.TMember;
import com.atguigu.scw.project.bean.TReturn;
import com.atguigu.scw.project.service.ProjectService;
import com.atguigu.scw.project.vo.request.ProjectBaseInfoVo;
import com.atguigu.scw.project.vo.request.ProjectRedisStorageVo;
import com.atguigu.scw.project.vo.request.ProjectReturnVo;
import com.atguigu.scw.project.vo.request.ProjectStepThreeInfoVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author erdong
 * @create 2019-10-06 11:23
 */
@RestController // 响应的结果都是json格式
@Slf4j
public class ProjectCreateController {

    @Autowired
    OssTemplate ossTemplate;

    @Autowired
    StringRedisTemplate redisTemplate;

    // 阅读并同意协议：创建big vo 对象，并分配唯一的token,将token返回
    @ApiOperation("阅读并同意协议")
    @PostMapping("/project/create/initProject")
    public AppResponse<Object> initProject(String accessToken) { // 必须提供accessToken
        if (StringUtils.isEmpty(accessToken)) {
            return AppResponse.fail(null, "发布项目必须登录！");
        }
        // 验证用户是否登录
        String memberStr = redisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberStr)) {
            return AppResponse.fail(null, "登录超时，请重新登录！");
        }
        // 创建bigvo对象，设置初始化的属性值
        ProjectRedisStorageVo bigVo = new ProjectRedisStorageVo();
        bigVo.setAccessToken(accessToken);

        // 转为TMember对象
        TMember tMember = JSON.parseObject(memberStr, TMember.class);
        bigVo.setMemberid(tMember.getId());
        // 将vo信息存到redis中
        // 创建唯一字符串：projectToken
        String projectToken = UUID.randomUUID().toString().replace("-", "");
        bigVo.setProjectToken(projectToken);
        // 将vo对象转为json字符串才可以存入redis:存之前一定要将相关的属性值设置之后再转换
        String bigVoStr = JSON.toJSONString(bigVo);
        redisTemplate.opsForValue().set(AppConsts.PROJECT_CREATE_TEMP_PREFIX + projectToken, bigVoStr, 7, TimeUnit.DAYS);
        // 返回成功信息+projectToken
        return AppResponse.ok(bigVo, "项目初始化成功");

    }

    // 第一步： 收集项目及发起人信息：修改 上一步的big vo的相对应的属性值
    @ApiOperation("第一步：项目及发起人信息")
    @PostMapping("/project/create/stepOne")
    public AppResponse<Object> stepOne(ProjectBaseInfoVo vo) {
        if (StringUtils.isEmpty(AppConsts.PROJECT_CREATE_TEMP_PREFIX + vo.getProjectToken())) {
            return AppResponse.fail(null, "发布项目必须登录！");
        }
        // 验证用户是否登录
        String memberStr = redisTemplate.opsForValue().get(AppConsts.PROJECT_CREATE_TEMP_PREFIX + vo.getProjectToken());
        if (StringUtils.isEmpty(memberStr)) {
            return AppResponse.fail(null, "登录超时，请重新登录！");
        }
        //1.获取redis中缓存的bigVo对象
        //String bigVoStr = redisTemplate.opsForValue().get(AppConsts.PROJECT_CREATE_TEMP_PREFIX + vo.getProjectToken());
        // 转为ProjectRedisStorageVo类Bean的Json格式
        //ProjectRedisStorageVo bigVo = JSON.parseObject(bigVoStr, ProjectRedisStorageVo.class);
        // 优化：调用封装好的ScwUtils
        ProjectRedisStorageVo bigVo = ScwUtils.getBeanFromRedis(redisTemplate, AppConsts.PROJECT_CREATE_TEMP_PREFIX + vo.getProjectToken(), ProjectRedisStorageVo.class);
        //2.将收集到的数据对象vo拷贝到bigVo对象
        BeanUtils.copyProperties(vo, bigVo);
        //3.将修改后的bigVo设置到redis中替换之前的
        String bigVoStr = JSON.toJSONString(bigVo);

        log.info("第一步数据封装之后的bigvo对象：{}", bigVoStr);
        redisTemplate.opsForValue().set(AppConsts.PROJECT_CREATE_TEMP_PREFIX + vo.getProjectToken(), bigVoStr);
        //4.成功响应
        return AppResponse.ok(bigVo, "项目及发起人信息收集成功");
    }

    // 第二步：收集回报信息
    @ApiOperation("第二步：回报信息")
    @PostMapping("/project/create/stepTwo")
    public AppResponse<Object> stepTwo(@RequestBody List<ProjectReturnVo> rtns) {
        // 判断用户是否登录
        if (CollectionUtils.isEmpty(rtns)) {
            return AppResponse.fail(null, "请上传t回报信息!!");
        }
        ProjectReturnVo vo = rtns.get(0);
        if (StringUtils.isEmpty(vo.getAccessToken())) {
            return AppResponse.fail(null, "发布项目必须登录!!!");
        }
        // 验证用户是否登录
        String memberStr = redisTemplate.opsForValue().get(vo.getAccessToken());
        if (StringUtils.isEmpty(memberStr)) {
            return AppResponse.fail(null, "登录超时，请重新登录!!!");
        }
        // 获取reids中bigVo对象
        String projectToken = vo.getProjectToken();
        ProjectRedisStorageVo bigVo = ScwUtils.getBeanFromRedis(redisTemplate,
                AppConsts.PROJECT_CREATE_TEMP_PREFIX + projectToken, ProjectRedisStorageVo.class);
        // 将ProjectReturnVo集合转为Tretuen集合设置给bigVo
        // 将接受到的回报的vo对象集合转为TReturn集合
        ArrayList<TReturn> list = new ArrayList<TReturn>();
        for (ProjectReturnVo projectReturnVo : rtns) {
            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(projectReturnVo, tReturn);
            list.add(tReturn);
        }
        bigVo.setProjectReturns(list);
        // 将修改后的bigVo重新设置到redis中缓存
        ScwUtils.saveBeanToRedis(bigVo, redisTemplate, bigVo.getProjectToken(),
                AppConsts.PROJECT_CREATE_TEMP_PREFIX);

        return AppResponse.ok(bigVo, "回报信息上传成功");
    }

    // 第三不：收集法人信息并提交：持久化数据到数据库中
    @Autowired
    ProjectService projectService;

    // 第三步：收集法人信息并提交: 持久化数据到数据库中
    @ApiOperation("第三步:收集法人信息并提交")
    @PostMapping("/project/create/submitProject")
    public AppResponse<Object> stepThree(Integer submitType, ProjectStepThreeInfoVo vo) {
        //submitType:提交的类型，  0代表草稿(存到临时表中，只有发起人可以查看) ， 1代表提交(保存到发布成功的项目表中)
        if (submitType == 1) {
            // 判断用户是否登录
            if (StringUtils.isEmpty(vo.getAccessToken())) {
                return AppResponse.fail(null, "发布项目必须登录!!!");
            }
            // 验证用户是否登录
            String memberStr = redisTemplate.opsForValue().get(vo.getAccessToken());
            if (StringUtils.isEmpty(memberStr)) {
                return AppResponse.fail(null, "登录超时，请重新登录!!!");
            }
            //获取redis中缓存的bigVo对象，将法人信息设置给bigVo
            ProjectRedisStorageVo bigVo = ScwUtils.getBeanFromRedis(redisTemplate, AppConsts.PROJECT_CREATE_TEMP_PREFIX + vo.getProjectToken(), ProjectRedisStorageVo.class);
            BeanUtils.copyProperties(vo, bigVo);
            //将bigVo的数据转为数据库表对应的javabean对象，然后调用mapper的方法存到对应的表中
            projectService.saveProject(bigVo);
            //删除reids中的缓存
            redisTemplate.delete(AppConsts.PROJECT_CREATE_TEMP_PREFIX + vo.getProjectToken());

            return AppResponse.ok(bigVo, "项目发布成功");
        } else {

            return AppResponse.ok(null, "保存项目草稿成功");
        }


    }

    // 测试文件上传
    @ApiOperation("上传项目图片")
    @PostMapping("/project/upLoadImgs")   // 上传文件:表单请求方式post,表单的entype="form-data/multipart"  服务器需要以多部件的形式解析表单上传的数据
    public AppResponse<Object> upLoadImg(MultipartFile[] imgs) {
        if (imgs != null) {
            List<String> listPath = new ArrayList(); // 图片上传成功保存图片路径的集合
            int count = 0; // 统计上传成功的数量
            for (MultipartFile img : imgs) {
                try {
                    String upLoadImgPath = ossTemplate.upLoadImg(img);
                    listPath.add(upLoadImgPath);
                    count++;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return AppResponse.fail(e.getMessage(), "图片上传意外~");
                }
            }
            // 给用户响应
            return AppResponse.ok(listPath, "上传成功：" + count + "张图片，失败：" + (imgs.length - count) + "张");
        } else {
            return AppResponse.fail(null, "没有读取到文件");
        }
    }

}
