package org.shared.utils;

import lombok.experimental.UtilityClass;
import org.shared.dto.ErrorDto;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;
@UtilityClass
public class ValidationMessageExtractor {
    public static List<ErrorDto> extractErrors(WebExchangeBindException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ErrorDto(fieldError.getDefaultMessage(), fieldError.getField()))
                .collect(Collectors.toList());
    }
}
