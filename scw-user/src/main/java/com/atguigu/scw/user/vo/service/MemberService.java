package com.atguigu.scw.user.vo.service;

import com.atguigu.scw.user.bean.TMember;
import com.atguigu.scw.user.vo.request.MemberRequestVo;

/**
 * @author erdong
 * @create 2019-09-28 8:22
 */
public interface MemberService {
    void saveMember(MemberRequestVo vo);

    TMember getMember(String username, String password);
}
