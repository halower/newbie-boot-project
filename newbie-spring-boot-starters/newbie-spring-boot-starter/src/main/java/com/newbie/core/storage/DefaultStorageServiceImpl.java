package com.newbie.core.storage;

import com.newbie.core.exception.BusinessException;
import com.newbie.core.exception.ResponseTypes;
import com.newbie.core.storage.config.StorageConfig;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("default_storage_service")
@ComponentScan("com.newbie.core.storage")
public class DefaultStorageServiceImpl implements StorageService {
    @Autowired
    private HttpServletResponse response;

    @Autowired
    private StorageConfig storageConfig;

    @Autowired
    private Environment env;


    @Override
    public String save(MultipartFile file) {
        var rootDir = Paths.get(env.getProperty("user.dir") + "/" + storageConfig.getBucket());
        if (!Files.exists(rootDir)) {
            try {
                Files.createDirectory(rootDir);
            } catch (IOException e) {
                throw new BusinessException(ResponseTypes.FAIL);
            }
        }
        try {
            Files.copy(file.getInputStream(), rootDir.resolve(file.getOriginalFilename()));
            return String.format("/%s/%s",storageConfig.getBucket(), file.getOriginalFilename());
        } catch (IOException e) {
            throw new BusinessException(ResponseTypes.File_SAVE_FAIL);
        }
    }

    @Override
    public void delete(String filename) {
        var rootDir = Paths.get(env.getProperty("user.dir") + "/" +  storageConfig.getBucket());
        Path path = rootDir.resolve(filename);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
          throw new BusinessException(ResponseTypes.FILE_DELETE_FAIL);
        }
    }

    @Override
    public void get(String filename) {
        try {
            var rootDir = Paths.get(env.getProperty("user.dir")  +"/" + storageConfig.getBucket());
            var path = rootDir + "/" + filename;
            var file = new File(path);
            var contentType  = Files.probeContentType(Paths.get(path) );
            response.setHeader("content-type", contentType);
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            if (!file.exists()) return;
            try (
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                    BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            ) {
                byte[] b = new byte[1024 * 5];
                int len;
                while ((len = inputStream.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                    outputStream.flush();
                }
            }
        }
        catch (IOException ex) {
            throw new BusinessException(ResponseTypes.FILE_DIR_NOT_FOUND);
        }
    }
}
