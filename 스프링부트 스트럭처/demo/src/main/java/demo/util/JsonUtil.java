package demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtil {

    /**
     * ObjectMapper 인스턴스를 생성하고 모듈을 등록하며 빈 객체에 대한 직렬화를 비활성화합니다.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    /**
     * 객체를 JSON 문자열로 변환합니다.
     *
     * @param obj 변환할 객체
     * @return JSON 문자열 또는 null
     */
    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * JSON 문자열을 주어진 클래스 타입으로 파싱합니다.
     *
     * @param json JSON 문자열
     * @param clazz 클래스 타입
     * @param <T> 반환 타입
     * @return 변환된 객체 또는 null
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * pretty-print JSON 문자열로 변환합니다.
     *
     * @param obj 대상 객체
     * @return 포맷된 JSON 문자열
     */
    public String toPrettyJson(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
