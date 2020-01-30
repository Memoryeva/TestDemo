package com.local.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("corporation")
public class Corporation extends Model<Corporation> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "corporation_id", type = IdType.AUTO)
    private Integer corporationId;

    private Integer corporationSuper;

    private String corporationName;

    @Override
    protected Serializable pkVal() {
        return this.corporationId;
    }
}
