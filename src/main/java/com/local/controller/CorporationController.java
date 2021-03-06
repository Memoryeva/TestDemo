package com.local.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.local.model.Corporation;
import com.local.service.CorporationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/corporation")
public class CorporationController extends ApiController {

    @Autowired
    private CorporationService corporationService;

    @GetMapping("/testCorporation")
    R<JSONObject> testMsg() {
        return success(new JSONObject() {{
            put("msg", "corporation");
        }});
    }

    @GetMapping("/testList")
    R<JSONArray> testList() {
        List<Corporation> list = corporationService.list();
        String str = JSON.toJSONStringWithDateFormat(list, "yyyy-MM-dd");
        JSONArray array = JSONArray.parseArray(str);
        return success(array);
    }
}
