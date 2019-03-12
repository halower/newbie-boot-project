package com.newbie.core.storage;
import com.newbie.core.dto.ResponseResult;
import com.newbie.core.exception.ResponseTypes;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@RestController
@RequestMapping("/api/storage")
@ComponentScan("com.newbie.core.storage")
public class StorageController {
    @Resource(name ="${newbie.storage.service:default_storage_service}")
    private StorageService storageService;

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public void download(@PathVariable String filename) {
        storageService.get(filename);
    }

    @PostMapping("/")
    public ResponseResult<String> upload(@RequestParam("file") MultipartFile file) {
        storageService.save(file);
        return new ResponseResult<>(ResponseTypes.SUCCESS,"上传成功", file.getOriginalFilename());
    }

    @DeleteMapping("/{filename:.+}")
    public ResponseResult<String> deleteFile(@PathVariable  String filename) {
        storageService.delete(filename);
        return new ResponseResult<>(ResponseTypes.SUCCESS,"删除成功",filename);
    }
}

