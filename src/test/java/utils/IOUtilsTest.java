package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.Map;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @Test
    void extractUserTest(){
        String path = "/user/create?userId=cu&password=password&name=%EC%9D%B4%EB%8F%99%EA%B7%9C&email=brainbackdoor%40gmail.com";
        Map<String, String> expected = Map.of("userId", "cu", "password", "password", "name", "이동규", "email", "brainbackdoor@gmail.com");
        Map<String,String> actual = IOUtils.extractUserFromPath(path);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
