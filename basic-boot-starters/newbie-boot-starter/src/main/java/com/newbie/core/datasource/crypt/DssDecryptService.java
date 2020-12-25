
package com.newbie.core.datasource.crypt;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class DssDecryptService {
	private String apiVer = "20200310";
	private String appToken="66f0184b-f685-4b1c-8b85-c8b03a4e388a";
	private String iv="ABEiM0RVZneImaq7zN3u/w==";
	private String dek="0RKsfovB3kzd4DtOZPu6TxE75IrZ19R60GfzxzD9a70=";

	public Map decrypt(Environment environment, DssDecryptInputDTO inputDTO) {
		String sfqy = environment.getProperty("sjjm.sfqy");
		String fwqdz = environment.getProperty("sjjm.fwqdz");
		Map resultMap = Maps.newHashMap();
		String cipher = inputDTO.getCipher();
		if (Objects.equals("Y", sfqy)) {
			String url = fwqdz + "/application/" + appToken + "/decrypt?apiVer=" + apiVer;
			Map params = Maps.newHashMap();
			params.put("Cipher", cipher);
			params.put("IV", iv);
			params.put("DEK", dek);
			String result = new RestTemplate().postForObject(url, params,String.class);
			DssResultDTO resultDTO = JSON.parseObject(result, DssResultDTO.class);
			resultMap.put("Plain", new String(Base64.getDecoder().decode(resultDTO.getPlain())));
		} else {
			resultMap.put("Plain", cipher);
		}
		return resultMap;
	}


	public Map decrypt(Environment environment, CustomDecryptInputDTO inputDTO) {
		Map<String, Object> decryptMap = inputDTO.getDecryptMap();
		if (CollectionUtils.isEmpty(decryptMap)) {
			return decryptMap;
		}
		Map resultMap = Maps.newHashMap();
		decryptMap.entrySet().parallelStream().forEach(map -> {
			String key = map.getKey();
			DssDecryptInputDTO dssDecryptInputDTO = new DssDecryptInputDTO();
			dssDecryptInputDTO.setCipher(map.getValue().toString());
			Map dssMap = decrypt(environment, dssDecryptInputDTO);
			resultMap.put(key, dssMap.get("Plain"));
		});
		return resultMap;
	}

}
