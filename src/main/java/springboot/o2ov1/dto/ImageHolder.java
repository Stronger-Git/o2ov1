package springboot.o2ov1.dto;

import java.io.InputStream;

public class ImageHolder {
    private String fileName;
    private InputStream inputStream;

    public ImageHolder() {}
    public ImageHolder(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
