package org.projects.productservice.api.handler;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.projects.productservice.api.dto.ErrorDto;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@RequiredArgsConstructor
@Controller
public class CustomErrorController implements ErrorController {

  private final ErrorAttributes errorAttributes;

  @RequestMapping("/error")
  public ResponseEntity<ErrorDto> error(WebRequest request) {
    Map<String, Object> attributes = errorAttributes.getErrorAttributes(
        request,
        ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION,
                                 ErrorAttributeOptions.Include.MESSAGE));

    Object statusObject = attributes.get("status");
    int status =
        (statusObject instanceof Integer ? (Integer)statusObject : 500);

    return ResponseEntity.status(status).body(
        ErrorDto.builder()
            .error((String)attributes.getOrDefault("error", "Unkown error"))
            .errorDescription(
                (String)attributes.getOrDefault("message", "No message"))
            .build());
  }
}
