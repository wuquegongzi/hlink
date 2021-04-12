package com.haibao.admin.web.service;

import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TUdf;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 自定义函数信息定义表 服务类
 * </p>
 *
 * @author zc
 * @since 2020-02-25
 */
public interface UdfService extends IService<TUdf> {

    Response addUdf(MultipartFile multfile,TUdf tUdf);

    Response deleteUdf(Long id);

    Response updateUdf(MultipartFile multfile,TUdf tUdf);

}
