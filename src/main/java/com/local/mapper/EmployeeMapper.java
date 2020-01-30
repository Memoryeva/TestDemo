package com.local.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.local.model.Employee;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeMapper extends BaseMapper<Employee> {

}
