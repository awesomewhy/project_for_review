package com.ozon.online.dto.mfa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull
@NotEmpty
@NotBlank
public class MfaVerificationRequest {
    private String nickname;
    private String code;
}
