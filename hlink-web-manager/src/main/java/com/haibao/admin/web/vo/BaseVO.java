package com.haibao.admin.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * VO 公共
 */
@ApiModel("公共 视图")
public class BaseVO implements Serializable {

    private static final long serialVersionUID = -8274758873447809336L;

    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页
     * */
    @ApiModelProperty(value = "当前页")
    private Integer curPage = 1;
    /**
     * 页面大小
     * */
    @ApiModelProperty(value = "页面大小")
    @Range(min = 1,max = 1000,message = "一次性获取最大列表数不能超过1000")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 自定义参数
     */
    @ApiModelProperty(value = "自定义参数")
    private Map parms;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;


    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map getParms() {
        return parms;
    }

    public void setParms(Map parms) {
        this.parms = parms;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "BaseVO{" +
                "curPage=" + curPage +
                ", pageSize=" + pageSize +
                ", parms=" + parms +
                ", createBy='" + createBy + '\'' +
                ", modifyBy='" + modifyBy + '\'' +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
