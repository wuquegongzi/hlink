package com.haibao.admin.web.service.impl;

import com.haibao.admin.web.service.JobDsSinkService;
import com.haibao.admin.web.entity.TJobDsSink;
import com.haibao.admin.web.mapper.JobDsSinkMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作业对应的数据源选择结果 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-03-05
 */
@Service
public class JobDsSinkServiceImpl extends ServiceImpl<JobDsSinkMapper, TJobDsSink> implements JobDsSinkService {

}
