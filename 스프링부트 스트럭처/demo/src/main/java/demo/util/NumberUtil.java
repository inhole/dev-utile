package demo.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class NumberUtil {

    /**
     * 문자열이 숫자인지 확인합니다.
     *
     * @param str 문자열
     * @return 숫자면 true
     */
    public boolean isNumeric(String str) {
        if (str == null || str.isBlank()) return false;
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * 문자열을 int로 안전하게 변환합니다.
     */
    public int toInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 문자열을 long으로 안전하게 변환합니다.
     */
    public long toLong(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 문자열을 double로 안전하게 변환합니다.
     */
    public double toDouble(String str, double defaultValue) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 두 숫자 중 더 작은 값을 반환합니다.
     */
    public int min(int a, int b) {
        return Math.min(a, b);
    }

    /**
     * 두 숫자 중 더 큰 값을 반환합니다.
     */
    public int max(int a, int b) {
        return Math.max(a, b);
    }

    /**
     * 소수를 반올림하여 소수점 이하 자리수를 제한합니다.
     *
     * @param value 대상 숫자
     * @param scale 소수점 자리수
     * @return 반올림된 double 값
     */
    public double round(double value, int scale) {
        return BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 숫자가 범위 안에 있는지 확인합니다.
     *
     * @param value 확인할 값
     * @param min 최소값
     * @param max 최대값
     * @return 범위 안에 있으면 true
     */
    public boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * 퍼센트 계산 (소수점 2자리)
     *
     * @param part 부분값
     * @param total 전체값
     * @return 퍼센트 (예: 75.25)
     */
    public double percent(int part, int total) {
        if (total == 0) return 0.0;
        return round((part * 100.0) / total, 2);
    }

    /**
     * 안전한 나눗셈 처리
     */
    public double safeDivide(double numerator, double denominator) {
        if (denominator == 0) return 0.0;
        return numerator / denominator;
    }
}
