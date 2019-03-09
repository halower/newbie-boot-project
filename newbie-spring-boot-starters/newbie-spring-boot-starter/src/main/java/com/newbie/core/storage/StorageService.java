package com.newbie.core.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService  {
    /**
     *  获取文件.
     * @param filename 要获取的文件.
      * @return 文件
     */
    void get(String filename);
    /**
     *  传文件.
     * @param file 要上传的文件.
     */
    String save(MultipartFile file);
    /**
     *  删除文件.
     * @param filename 要删除的文件名.
     */
    void delete(String filename);
}
