package com.haibao.admin.web.service;

import com.haibao.admin.web.entity.TJobLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-04-08
 */
public interface JobLogService extends IService<TJobLog> {

    void sendLog(Long id, String gsonString, String s, int msgType);
}
