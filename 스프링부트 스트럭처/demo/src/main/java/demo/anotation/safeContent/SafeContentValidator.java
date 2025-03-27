package demo.anotation.safeContent;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SafeContentValidator implements ConstraintValidator<SafeContent, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        // XSS 방지
        boolean isValid = Jsoup.isValid(value, Safelist.basic());
        if (!isValid) {
            setCustomMessage(context, "입력값에 유효하지 않은 태그가 포함되어 있습니다.");
            return false;
        }

        // 비속어 검증
        for (String badWord : BadWords.BAD_WORDS) {
            if (value.toLowerCase().contains(badWord)) {
                setCustomMessage(context, String.format("%s은/는 사용할 수 없습니다.", badWord));
                return false;
            }
        }
        return true;
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
