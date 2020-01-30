package com.local.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("department")
public class Department extends Model<Department> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "department_id", type = IdType.AUTO)
    private Integer departmentId;

    private Integer departmentSuper;

    private String departmentName;

    @Override
    protected Serializable pkVal() {
        return this.departmentId;
    }
}
