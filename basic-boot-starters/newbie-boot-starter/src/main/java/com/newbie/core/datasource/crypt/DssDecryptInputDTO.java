package com.newbie.core.datasource.crypt;

import com.newbie.dto.InputDTO;
import lombok.Data;

@Data
public class DssDecryptInputDTO implements InputDTO {
    private String cipher;
}