package com.local.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.local.mapper.CorporationMapper;
import com.local.model.Corporation;
import com.local.service.CorporationService;
import org.springframework.stereotype.Service;

@Service
public class CorporationServiceImpl extends ServiceImpl<CorporationMapper, Corporation> implements CorporationService {

}
