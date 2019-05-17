package com.newbie.core.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 谢海龙
 * @Date: 2019/5/11 15:43
 * @Description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserInfo {
    private String dlbm;
    private String dwbm;
    private String dwmc;
    private String rybm;
}
