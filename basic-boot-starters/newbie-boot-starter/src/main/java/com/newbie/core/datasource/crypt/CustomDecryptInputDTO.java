package com.newbie.core.datasource.crypt;

import com.newbie.dto.InputDTO;
import lombok.Data;

import java.util.Map;

@Data
public class CustomDecryptInputDTO implements InputDTO {

	private Map<String, Object> decryptMap;
}
