package demo.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@UtilityClass
public class FileUtil {

    /**
     * 파일명에서 확장자를 반환합니다.
     *
     * @param filename 파일 이름
     * @return 확장자 (없으면 빈 문자열)
     */
    public String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    /**
     * 파일명에서 확장자를 제외한 이름만 반환합니다.
     *
     * @param filename 파일 이름
     * @return 확장자를 제외한 이름
     */
    public String getNameWithoutExtension(String filename) {
        if (filename == null) return "";
        int index = filename.lastIndexOf(".");
        return (index != -1) ? filename.substring(0, index) : filename;
    }

    /**
     * 파일이 존재하는지 확인합니다.
     *
     * @param path 파일 경로
     * @return 존재하면 true
     */
    public boolean exists(Path path) {
        return path != null && Files.exists(path);
    }

    /**
     * 디렉토리 여부를 확인합니다.
     *
     * @param file 파일 객체
     * @return 디렉토리면 true
     */
    public boolean isDirectory(File file) {
        return file != null && file.isDirectory();
    }

    /**
     * 파일이 비어있는지 확인합니다.
     *
     * @param file 파일 객체
     * @return 비어있으면 true
     */
    public boolean isEmpty(File file) {
        return file == null || file.length() == 0;
    }
}
