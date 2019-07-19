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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 金额操作
 *
 */
public class AmountUtil {

    private static final Pattern AMOUNT_PATTERN =
    Pattern.compile("^(0|[1-9]\\d{0,11})\\.(\\d\\d)$"); // 不考虑分隔符的正确性
    private static final char[] RMB_NUMS = "零壹贰叁肆伍陆柒捌玖".toCharArray();
    private static final String[] UNITS = {"元", "角", "分", "整"};
    private static final String[] U1 = {"", "拾", "佰", "仟"};
    private static final String[] U2 = {"", "万", "亿"};

    /**
     * 将金额（整数部分等于或少于12位，小数部分2位）转换为中文大写形式.
     *
     * @param amount 金额数字
     * @return 中文大写
     */
    public String convert(String amount) throws IllegalArgumentException {
        amount = amount.replace(",", "");
        String[] amountArray = amount.split("\\.");
        if (amountArray.length != 2) {
            amount = amount + ".00";
        } else {
            if (amountArray[1].length() == 1) {
                amount = amount + "0";
            }
        }
        if (amount.equals("0.00")) {
            return "零元整";
        }
        Matcher matcher = AMOUNT_PATTERN.matcher(amount);
        if (!matcher.find()) {
            throw new IllegalArgumentException("输入金额有误.");
        }
        String integer = matcher.group(1); // 整数部分
        String fraction = matcher.group(2); // 小数部分
        StringBuilder sb = new StringBuilder("");
        if (!integer.equals("0")) {
            sb.append(integer2rmb(integer)).append(UNITS[0]); // 整数部分
        }
        if (fraction.equals("00")) {
            sb.append(UNITS[3]); // 添加[整]
        } else if (fraction.startsWith("0") && integer.equals("0")) {
            sb.append(fraction2rmb(fraction).substring(1)); // 去掉分前面的[零]
        } else {
            sb.append(fraction2rmb(fraction)); // 小数部分
        }
        return sb.toString();
    }

    // 将金额小数部分转换为中文大写
    private static String fraction2rmb(String fraction) {
        char jiao = fraction.charAt(0); // 角
        char fen = fraction.charAt(1); // 分
        return (RMB_NUMS[jiao - '0'] + (jiao > '0' ? UNITS[1] : ""))
                + (fen > '0' ? RMB_NUMS[fen - '0'] + UNITS[2] : "");
    }

    // 将金额整数部分转换为中文大写
    private static String integer2rmb(String integer) {
        StringBuilder buffer = new StringBuilder();
        // 从个位数开始转换
        int i, j;
        for (i = integer.length() - 1, j = 0; i >= 0; i--, j++) {
            char n = integer.charAt(i);
            if (n == '0') {
                // 当n是0且n的右边一位不是0时，插入[零]
                if (i < integer.length() - 1 && integer.charAt(i + 1) != '0') {
                    buffer.append(RMB_NUMS[0]);
                }
                // 插入[万]或者[亿]
                if (j % 4 == 0) {
                    if (i > 0 && integer.charAt(i - 1) != '0'
                            || i > 1 && integer.charAt(i - 2) != '0'
                            || i > 2 && integer.charAt(i - 3) != '0') {
                        buffer.append(U2[j / 4]);
                    }
                }
            } else {
                if (j % 4 == 0) {
                    buffer.append(U2[j / 4]);     // 插入[万]或者[亿]
                }
                buffer.append(U1[j % 4]);         // 插入[拾]、[佰]或[仟]
                buffer.append(RMB_NUMS[n - '0']); // 插入数字
            }
        }
        return buffer.reverse().toString();
    }

}
