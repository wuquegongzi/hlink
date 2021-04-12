package com.haibao.admin.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.haibao.admin.web.vo.DsVO;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TDs;
import com.haibao.admin.web.vo.templete.JsonField;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface DsService extends IService<TDs> {

    Response<Map> jsonSchemaSubmitAndAnalysis(MultipartFile file, HttpServletRequest req);

    Long saveDs(DsVO ds) throws Exception;

    boolean checkJsonFields(List<JsonField> jsonFields);

    Response check(DsVO dsVO);

    Response<DsVO> get(DsVO dsVO);

    Response getFieldTemplete(DsVO dsVO);

    Response delete(Long id);

    Response modify(DsVO dsVO);

    Response checkTableName(String tbname,Long id);
}
