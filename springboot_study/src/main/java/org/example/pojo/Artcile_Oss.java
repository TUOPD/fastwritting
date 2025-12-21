package org.example.pojo;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Profile("prod")
public class Artcile_Oss {
    private static String endpoint ="xxxxxxx";
    private static String accessKeyId = "xxxxxxxxxxxxxx" ;
    private static String accessKeySecret = "xxxxxxxxxxxxxxxxx";
    private static String bucketName = "xxxxxxxxxxxxx";
    private static String prefix="xxxxxxxxxxx";


    public static String upload(MultipartFile file,String  loacalID ) {
        String fileName =  loacalID + "-" + file.getOriginalFilename();
        String objectName = prefix + fileName;

        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            try (InputStream inputStream = file.getInputStream()) {
                ossClient.putObject(bucketName, objectName, inputStream);
            }

            return "http://" + bucketName + "." + endpoint + "/" + objectName;
        } catch (Exception e) {
            throw new RuntimeException("上传失败：" + e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}