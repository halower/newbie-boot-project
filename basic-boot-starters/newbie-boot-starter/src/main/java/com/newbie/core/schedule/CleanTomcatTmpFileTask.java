package com.newbie.core.schedule;

import com.newbie.common.api.JsonResult;
import com.newbie.core.exception.Asserts;
import io.netty.util.internal.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;

@Log4j2
@ConditionalOnProperty(name="application.tomcat.auto-clean", havingValue="true")
public class CleanTomcatTmpFileTask {
    @Value("${server.tomcat.basedir}")
    private String tmp_dir;
    private final String AUTO_CREATED_FOLDER = "/work/Tomcat/localhost/ROOT";

    @Scheduled(cron ="${application.tomcat.clean-corn}")
    public void deleteTmpFileTask() {
        if(StringUtil.isNullOrEmpty(tmp_dir)){
            log.error("Tomcat临时目录未指定");
            Asserts.fail("Tomcat临时目录未指定");
        }
        File targetFile = new File(tmp_dir);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        log.info("开始清理");
        String upload_tmp = tmp_dir.trim() + AUTO_CREATED_FOLDER;
        File deleteFile = new File(upload_tmp);
        File[] deleteFiles = deleteFile.listFiles();
        for (File file : deleteFiles) {
            deleteFile(file);

        }
        log.info("清理结束");
    }


    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }

            if (file.listFiles().length == 0) {
                file.delete();
            }
        } else {
            file.delete();
        }
    }
}
