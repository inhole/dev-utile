package demo.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@UtilityClass
public class DateUtil {

    /**
     * LocalDate를 yyyy-MM-dd 형식 문자열로 변환합니다.
     *
     * @param date 날짜 객체
     * @return yyyy-MM-dd 형식의 문자열
     */
    public String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 문자열을 LocalDate로 파싱합니다.
     *
     * @param dateStr yyyy-MM-dd 형식의 문자열
     * @return LocalDate 객체
     */
    public LocalDate parse(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 현재 날짜를 yyyy-MM-dd 형식으로 반환합니다.
     *
     * @return 현재 날짜 문자열
     */
    public String today() {
        return format(LocalDate.now());
    }

    /**
     * 두 날짜 사이의 일 수를 계산합니다.
     *
     * @param from 시작일
     * @param to 종료일
     * @return 일(day) 단위 차이
     */
    public long daysBetween(LocalDate from, LocalDate to) {
        return ChronoUnit.DAYS.between(from, to);
    }

    /**
     * LocalDateTime을 yyyy-MM-dd HH:mm:ss 형식으로 포맷합니다.
     *
     * @param dateTime 날짜시간 객체
     * @return 포맷된 문자열
     */
    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 문자열을 LocalDateTime으로 파싱합니다.
     *
     * @param dateTimeStr yyyy-MM-dd HH:mm:ss 형식의 문자열
     * @return LocalDateTime 객체
     */
    public LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
