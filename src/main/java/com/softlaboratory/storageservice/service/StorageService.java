package com.softlaboratory.storageservice.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.softlaboratory.storageservice.constant.AppConstant;
import com.softlaboratory.storageservice.domain.dao.FileDao;
import com.softlaboratory.storageservice.domain.dto.FileDto;
import com.softlaboratory.storageservice.repository.FileRepository;
import com.softlaboratory.storageservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class StorageService {

    Storage storage;

    @Autowired
    FileRepository fileRepository;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("chatapp-ccd4e-firebase-adminsdk-1gf5o-87e9a79ca0.json");
            storage = StorageOptions.newBuilder().
                    setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())).
                    setProjectId("chatapp-ccd4e").build().getService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ResponseEntity<Object> getAllFile () {
        List<FileDao> fileDaoList = fileRepository.findAll();
        List<FileDto> fileDtoList = new ArrayList<>();

        for (FileDao dao: fileDaoList) {
            fileDtoList.add(FileDto.builder()
                            .id(dao.getId())
                            .fileName(dao.getFileName())
                            .fileType(dao.getFileType())
                            .fileUrl(dao.getFileUrl())
                    .build());
        }

        return ResponseUtil.build(HttpStatus.OK, "Get all file success" , fileDtoList);
    }

    @Async
    public ResponseEntity<Object> uploadFile(MultipartFile file) throws IOException {
        try {

            String fileName = generateFileName(file.getOriginalFilename());
            Map<String, String> map = new HashMap<>();
            map.put("firebaseStorageDownloadTokens", fileName);
            BlobId blobId = BlobId.of("chatapp-ccd4e.appspot.com", fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setMetadata(map)
                    .setContentType(file.getContentType())
                    .build();
            storage.create(blobInfo, file.getInputStream());

            String downloadUrl = String.format(AppConstant.DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));

            FileDao fileDao = FileDao.builder()
                    .fileName(file.getOriginalFilename())
                    .fileUrl(downloadUrl)
                    .fileType(file.getContentType())
                    .build();
            fileDao = fileRepository.save(fileDao);

            FileDto fileDto = FileDto.builder()
                    .id(fileDao.getId())
                    .fileName(fileDao.getFileName())
                    .fileUrl(fileDao.getFileUrl())
                    .fileType(fileDao.getFileType())
                    .build();

            return ResponseUtil.build(HttpStatus.OK, "Upload success.", null);
        }catch (Exception e) {
            throw e;
        }
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "." + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

}
