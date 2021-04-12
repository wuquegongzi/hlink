package com.haibao.admin.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haibao.admin.web.entity.TJob;
import com.haibao.admin.web.vo.JobVO;
import org.springframework.stereotype.Repository;

/**
 * Created by baoyu on 2020/2/5.
 * Describe
 */
@Repository
public interface JobMapper extends BaseMapper<TJob> {

    IPage<JobVO> selectJobList(Page<JobVO> page, JobVO jobQuery);
}
