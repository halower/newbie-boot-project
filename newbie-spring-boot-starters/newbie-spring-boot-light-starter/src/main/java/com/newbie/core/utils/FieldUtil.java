/*
 * Apache License
 *
 * Copyright (c) 2019  halower (halower@foxmail.com).
 *
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * 版权所有 (c) 2019-2029, halower (halower@foxmail.com).
 *
 * Apache 2.0 License 同时该协议为补充协议，不允许 996 工作制度企业使用该开源软件
 *
 * 反996许可证版本1.0
 *
 * 在符合下列条件的情况下，特此免费向任何得到本授权作品的副本（包括源代码、文件和/或相关内容，以下
 * 统称为“授权作品”）的个人和法人实体授权：被授权个人或法人实体有权以任何目的处置授权作品，包括但
 * 不限于使用、复制，修改，衍生利用、散布，发布和再许可：
 *
 * 1. 个人或法人实体必须在许可作品的每个再散布或衍生副本上包含以上版权声明和本许可证，不得自行修
 * 改。
 * 2. 个人或法人实体必须严格遵守与个人实际所在地或个人出生地或归化地、或法人实体注册地或经营地
 * （以较严格者为准）的司法管辖区所有适用的与劳动和就业相关法律、法规、规则和标准。如果该司法管辖
 * 区没有此类法律、法规、规章和标准或其法律、法规、规章和标准不可执行，则个人或法人实体必须遵守国
 * 际劳工标准的核心公约。
 * 3. 个人或法人不得以任何方式诱导、暗示或强迫其全职或兼职员工或其独立承包人以口头或书面形式同意直接或
 * 间接限制、削弱或放弃其所拥有的，受相关与劳动和就业有关的法律、法规、规则和标准保护的权利或补救
 * 措施，无论该等书面或口头协议是否被该司法管辖区的法律所承认，该等个人或法人实体也不得以任何方法
 * 限制其雇员或独立承包人向版权持有人或监督许可证合规情况的有关当局报告或投诉上述违反许可证的行为
 * 的权利。
 *
 * 该授权作品是"按原样"提供，不做任何明示或暗示的保证，包括但不限于对适销性、特定用途适用性和非侵
 * 权性的保证。在任何情况下，无论是在合同诉讼、侵权诉讼或其他诉讼中，版权持有人均不承担因本软件或
 * 本软件的使用或其他交易而产生、引起或与之相关的任何索赔、损害或其他责任。
 */
package com.newbie.core.utils;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 常用字段操作
 */
public class FieldUtil {

    private static final Pattern EMAIL_ADDRESS_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]*[A-Z0-9]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern MOBILE_PATTERN =
            Pattern.compile("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4\\D])|(18[0-9])|(166)|(199)|(198))\\d{8}$");

    private static final Pattern CHINESE_PATTERN =
            Pattern.compile("^[\u4e00-\u9fa5]+$");

    private static final Pattern IPV4_PATTERN =
            Pattern.compile(
                    "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final Pattern IPV6_STD_PATTERN =
            Pattern.compile(
                    "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
            Pattern.compile(
                    "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    FieldUtil() {
    }

    /**
     * 验证邮箱格式是否合法
     *
     * @param email 邮件
     */
    public boolean validateEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式是否合法
     *
     * @param mobile 手机号
     */
    public boolean validateMobile(String mobile) {
        return MOBILE_PATTERN.matcher(mobile).matches();
    }

    /**
     * 验证身份证是否合法
     *
     * @param idNumber 身份编号
     */
    public boolean validateIdNumber(String idNumber) {
        return IdcardUtils.validateCard(idNumber);
    }


    /**
     * 验证是否是IPv4
     *
     * @param str 待校验字符串
     */
    public boolean isIPv4Address(String str) {
        return IPV4_PATTERN.matcher(str).matches();
    }

    /**
     * 验证是否是IPv6
     *
     * @param str 待校验字符串
     */
    public boolean isIPv6Address(String str) {
        return isIPv6StdAddress(str) || isIPv6HexCompressedAddress(str);
    }

    private boolean isIPv6StdAddress(String str) {
        return IPV6_STD_PATTERN.matcher(str).matches();
    }

    private boolean isIPv6HexCompressedAddress(String str) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(str).matches();
    }

    /**
     * 是否是汉字
     *
     * @param str 待校验字符串
     */
    public boolean isChinese(String str) {
        return CHINESE_PATTERN.matcher(str).matches();
    }

    /**
     * 根据身份编号获取年龄
     *
     * @param idNumber 身份编号
     * @return 年龄
     */
    public int getAgeByIdCard(String idNumber) {
        return IdcardUtils.getAgeByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日
     *
     * @param idNumber 身份编号
     * @return 生日(yyyyMMdd)
     */
    public String getBirthByIdCard(String idNumber) {
        return IdcardUtils.getBirthByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日年
     *
     * @param idNumber 身份编号
     * @return 生日(yyyy)
     */
    public Short getYearByIdCard(String idNumber) {
        return IdcardUtils.getYearByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日月
     *
     * @param idNumber 身份编号
     * @return 生日(MM)
     */
    public Short getMonthByIdCard(String idNumber) {
        return IdcardUtils.getMonthByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取生日天
     *
     * @param idNumber 身份编号
     * @return 生日(dd)
     */
    public Short getDateByIdCard(String idNumber) {
        return IdcardUtils.getDateByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取性别
     *
     * @param idNumber 身份编号
     * @return 性别(M-男，F-女，N-未知)
     */
    public String getGenderByIdCard(String idNumber) {
        return IdcardUtils.getGenderByIdCard(idNumber);
    }

    /**
     * 根据身份编号获取户籍省份
     *
     * @param idNumber 身份编码
     * @return 省级编码。
     */
    public String getProvinceByIdCard(String idNumber) {
        return IdcardUtils.getProvinceByIdCard(idNumber);
    }


    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "$", "i", "j", "k", "l", "m", "n", "o", "p", "q", "$", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * 获取UUID
     *
     * @return UUID
     */
    public String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取短UUID
     *
     * @return 短UUID
     */
    public String createShortUUID() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = createUUID();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

}
