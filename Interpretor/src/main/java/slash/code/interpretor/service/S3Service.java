package slash.code.interpretor.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface S3Service {

    public ByteArrayOutputStream downloadFile(String keyName);

    public void uploadFile(String keyName, MultipartFile file);

    public List listFiles();
}
