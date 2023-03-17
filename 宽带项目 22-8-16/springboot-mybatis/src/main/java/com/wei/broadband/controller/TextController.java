package com.wei.broadband.controller;

import com.wei.broadband.common.R;
import com.wei.broadband.po.Text;
import com.wei.broadband.service.TextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/text")
@Slf4j
public class TextController {
    @Autowired
    TextService textService;

    String basePath = "C:/text/";

    @GetMapping("/list")
    public R<List<Text>> getList(){
        List<Text> list = textService.list();
        return R.success(list);
    }

    @PostMapping("/add")
    public R<Text> addText(@RequestBody Text text){

        String filename= UUID.randomUUID().toString()+".txt";

        try {
            File writeName = new File(basePath+filename); // 相对路径，如果没有则要建立一个新的output.txt文件
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖

        } catch (IOException e) {
            e.printStackTrace();
        }
        text.setTxtName(filename);
        textService.save(text);
        List<Text> list = textService.list();
        Text r = list.get(list.size()-1);

        return R.success(r);
    }

    @PostMapping("/update")
    public R<String> updateText(@RequestBody Text text){
        textService.updateById(text);
        return R.success("修改成功");
    }

    @GetMapping("/gettext")
    public R<Text> getTextById(int id){
        Text text = textService.getById(id);
        return R.success(text);
    }

    @GetMapping("/content")
    public R<String> getContentById(int id){
        Text text = textService.getById(id);
        String pathname = basePath+text.getTxtName();
        String r = "";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
               r += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(r);
    }

    @GetMapping("/save")
    public R<String> saveText(int id,String content){
        Text text = textService.getById(id);
        String txtName = text.getTxtName();
        try {
            File writeName = new File(basePath+txtName);
            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(content); // \r\n即为换行
                out.flush(); // 把缓存区内容压入文件
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success("保存成功");
    }
}
