package demo.util;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtil {

    /**
     * 문자열이 비어있는지 확인합니다.
     *
     * @param str 확인할 문자열
     * @return 문자열이 null이거나 비어있으면 true, 그렇지 않으면 false
     */
    public boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 문자열이 비어있지 않은지 확인합니다.
     *
     * @param str 확인할 문자열
     * @return 문자열이 null이 아니고 비어있지 않으면 true, 그렇지 않으면 false
     */
    public boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 문자열이 공백인지 확인합니다.
     *
     * @param str 확인할 문자열
     * @return 문자열이 null이거나 공백이면 true, 그렇지 않으면 false
     */
    public boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    /**
     * 문자열이 공백이 아닌지 확인합니다.
     *
     * @param str 확인할 문자열
     * @return 문자열이 null이 아니고 공백이 아니면 true, 그렇지 않으면 false
     */
    public boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 문자열의 첫 글자를 대문자로 변환합니다.
     *
     * @param str 변환할 문자열
     * @return 첫 글자가 대문자인 문자열
     */
    public String capitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 문자열의 첫 글자를 소문자로 변환합니다.
     *
     * @param str 변환할 문자열
     * @return 첫 글자가 소문자인 문자열
     */
    public String uncapitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 컬렉션의 요소들을 주어진 구분자로 연결합니다.
     *
     * @param items 연결할 컬렉션
     * @param delimiter 구분자
     * @return 연결된 문자열
     */
    public String join(Collection<?> items, String delimiter) {
        return items == null ? "" : String.join(delimiter, items.stream().map(Objects::toString).toList());
    }

    /**
     * 배열의 요소들을 주어진 구분자로 연결합니다.
     *
     * @param items 연결할 배열
     * @param delimiter 구분자
     * @return 연결된 문자열
     */
    public String join(String[] items, String delimiter) {
        return items == null ? "" : String.join(delimiter, items);
    }

    /**
     * 두 문자열을 대소문자 구분 없이 비교합니다.
     *
     * @param a 비교할 첫 번째 문자열
     * @param b 비교할 두 번째 문자열
     * @return 두 문자열이 같으면 true, 그렇지 않으면 false
     */
    public boolean equalsIgnoreCase(String a, String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * 문자열이 비어있으면 기본 문자열을 반환합니다.
     *
     * @param str 확인할 문자열
     * @param defaultStr 기본 문자열
     * @return 비어있지 않은 문자열 또는 기본 문자열
     */
    public String defaultIfEmpty(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    /**
     * 문자열을 주어진 횟수만큼 반복합니다.
     *
     * @param str 반복할 문자열
     * @param times 반복 횟수
     * @return 반복된 문자열
     */
    public String repeat(String str, int times) {
        return str == null || times <= 0 ? "" : str.repeat(times);
    }

    /**
     * 문자열을 주어진 최대 길이로 자릅니다.
     *
     * @param str 자를 문자열
     * @param maxLength 최대 길이
     * @return 자른 문자열
     */
    public String truncate(String str, int maxLength) {
        return (str == null || str.length() <= maxLength) ? str : str.substring(0, maxLength);
    }

    /**
     * 정규식을 이용해 문자열이 패턴에 일치하는지 확인합니다.
     *
     * @param input 대상 문자열
     * @param regex 정규식
     * @return 일치하면 true
     */
    public boolean matches(String input, String regex) {
        if (input == null || regex == null) return false;
        return Pattern.matches(regex, input);
    }

    /**
     * 이메일 형식인지 확인합니다.
     */
    public boolean isEmail(String input) {
        return matches(input, "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * 휴대폰 번호 형식인지 확인합니다. (예: 010-1234-5678)
     */
    public boolean isPhoneNumber(String input) {
        return matches(input, "^01[0-9]-\\d{3,4}-\\d{4}$");
    }

    /**
     * 숫자만 포함되었는지 확인합니다.
     */
    public boolean isNumeric(String input) {
        return matches(input, "\\d+");
    }

    /**
     * 문자열에서 정규식과 일치하는 첫 번째 그룹을 추출합니다.
     */
    public String extractFirstGroup(String input, String regex) {
        if (input == null || regex == null) return null;
        Matcher matcher = Pattern.compile(regex).matcher(input);
        return matcher.find() ? matcher.group(1) : null;
    }
}
