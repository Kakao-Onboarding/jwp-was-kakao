package utils;

import enums.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class FileIoUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(FileIoUtilsTest.class);

    @Test
    void loadFileFromClasspath() throws Exception {
        byte[] body = FileIoUtils.loadFileFromClasspath("./templates/index.html");
        log.debug("file : {}", new String(body));
    }

    @DisplayName("정적 파일을 경로를 반환하는지 확인한다")
    @Test
    void checkIfPathIsStaticFile() {
        //given
        String path = "/css/styles.css";

        //when
        ContentType contentType = ContentType.fromFilename(path);
        String resourcePath = FileIoUtils.getResourcePath(path, contentType);

        //then
        assertThat(resourcePath).startsWith("./static");
    }
}
