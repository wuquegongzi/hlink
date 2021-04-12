package com.haibao.admin.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haibao.admin.web.vo.ClusterVO;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TCluster;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2020-02-15
 */
public interface ClusterService extends IService<TCluster> {
    /**
     * 根据集群ID获取集群信息
     * */
    Response<ClusterVO> info(Long clusterId);

}
