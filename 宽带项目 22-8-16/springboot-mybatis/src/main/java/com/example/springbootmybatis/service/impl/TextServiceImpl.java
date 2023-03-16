package com.example.springbootmybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootmybatis.mapper.TextMapper;
import com.example.springbootmybatis.po.Text;
import com.example.springbootmybatis.service.TextService;
import org.springframework.stereotype.Service;

@Service
public class TextServiceImpl extends ServiceImpl<TextMapper, Text> implements TextService {
}
