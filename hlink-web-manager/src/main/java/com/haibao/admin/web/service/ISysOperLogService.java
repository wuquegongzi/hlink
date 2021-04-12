package com.haibao.admin.web.service;

import com.haibao.admin.web.entity.SysOperLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 操作日志记录 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-04-22
 */
public interface ISysOperLogService extends IService<SysOperLog> {

    void sendLog(SysOperLog sysOperLog);

}
