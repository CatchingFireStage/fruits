package me.fruits.fruits.service.upload;

import lombok.extern.slf4j.Slf4j;
import me.fruits.fruits.utils.FruitsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * 上传服务
 * 绝对路径  rootPath/project/module/year/month/day/文件名
 */
@Service
@Slf4j
public class UploadService {

    @Value("${fruits.upload.root-path}")
    private String uploadRootPath;

    @Value("${spring.application.name}")
    private String project;

    public String uploadBySpu(MultipartFile multipartFile) throws IOException, FruitsException {
        return this.upload("spu", multipartFile);
    }

    /**
     * 上传文件
     *
     * @param module        上传的模块
     * @param multipartFile 上传的文件
     * @return 返回上传的相对路径
     */
    private String upload(String module, MultipartFile multipartFile) throws IOException, FruitsException {


        UUID uuid = UUID.randomUUID();

        String suffixName = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));

        String fileName = String.format("%s%s", uuid.toString(), suffixName);

        LocalDateTime now = LocalDateTime.now();

        Path relativePath = Paths.get(project, module, String.format("%d", now.getYear()),
                String.format("%d", now.getMonth().getValue()),
                String.format("%d", now.getDayOfMonth()),
                fileName);

        Path absPath = Paths.get(uploadRootPath, relativePath.toString());

        File uploadFile = absPath.toFile();
        if(uploadFile.exists()){
            throw new FruitsException(FruitsException.DEFAULT_ERR,"文件已经存在，稍后重试");
        }

        //创建目录
        File dir = new File(uploadFile.getParent());
        dir.mkdirs();


        if(!uploadFile.createNewFile()){
            throw new FruitsException(FruitsException.DEFAULT_ERR,"文件创建失败，稍后重试");
        }

        multipartFile.transferTo(uploadFile);

        return relativePath.toString();
    }
}
