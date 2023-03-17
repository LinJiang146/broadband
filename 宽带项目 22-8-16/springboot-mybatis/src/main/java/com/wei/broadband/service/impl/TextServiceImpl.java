package com.wei.broadband.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.broadband.mapper.TextMapper;
import com.wei.broadband.po.Text;
import com.wei.broadband.service.TextService;
import org.springframework.stereotype.Service;

@Service
public class TextServiceImpl extends ServiceImpl<TextMapper, Text> implements TextService {
}
