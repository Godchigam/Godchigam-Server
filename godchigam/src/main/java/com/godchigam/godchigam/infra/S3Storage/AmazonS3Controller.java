
package com.godchigam.godchigam.infra.S3Storage;

import com.godchigam.godchigam.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class AmazonS3Controller {

    private final AwsS3Service awsS3Service;

    /**
     * Amazon S3에 파일 업로드
     * @return 성공 시 200 Success와 함께 업로드 된 파일의 파일명 리스트 반환
     */

    @PostMapping("")
    public CommonResponse uploadFile(@RequestPart MultipartFile multipartFile, @RequestParam String filePath) {
        return CommonResponse.success(awsS3Service.uploadFile(multipartFile,filePath),"이미지 업로드 성공");
    }

}