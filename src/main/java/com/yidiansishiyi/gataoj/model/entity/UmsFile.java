package com.yidiansishiyi.gataoj.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 图片
 * @TableName ums_file
 */
@TableName(value ="ums_file")
@Data
@AllArgsConstructor
public class UmsFile implements Serializable {
    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 标题
     */
    private String md5;

    /**
     * 标签列表（json 数组）
     */
    private Long size;

    /**
     * 题目答案
     */
    private String suffix;

    /**
     * 题目提交数
     */
    private String path;

    /**
     * 题目通过数
     */
    private String sort;

    /**
     * 是否删除
     */
    private Integer isDelete;

    public UmsFile(String id, String md5, Long size, String suffix, String path) {
        this.id = id;
        this.md5 = md5;
        this.size = size;
        this.suffix = suffix;
        this.path = path;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UmsFile other = (UmsFile) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMd5() == null ? other.getMd5() == null : this.getMd5().equals(other.getMd5()))
            && (this.getSize() == null ? other.getSize() == null : this.getSize().equals(other.getSize()))
            && (this.getSuffix() == null ? other.getSuffix() == null : this.getSuffix().equals(other.getSuffix()))
            && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
            && (this.getSort() == null ? other.getSort() == null : this.getSort().equals(other.getSort()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMd5() == null) ? 0 : getMd5().hashCode());
        result = prime * result + ((getSize() == null) ? 0 : getSize().hashCode());
        result = prime * result + ((getSuffix() == null) ? 0 : getSuffix().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getSort() == null) ? 0 : getSort().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", md5=").append(md5);
        sb.append(", size=").append(size);
        sb.append(", suffix=").append(suffix);
        sb.append(", path=").append(path);
        sb.append(", sort=").append(sort);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}