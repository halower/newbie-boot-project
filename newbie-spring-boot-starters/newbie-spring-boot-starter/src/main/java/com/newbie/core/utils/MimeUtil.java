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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MIME信息操作
 */
public class MimeUtil {

    MimeUtil(){}

    private  static Map<String, List<String>> types = new HashMap<String, List<String>>() {{
        put("office", new ArrayList<String>() {{
            add(Mime.TXT.toString());
            add(Mime.DOC.toString());
            add(Mime.DOCX.toString());
            add(Mime.XLS1.toString());
            add(Mime.XLS2.toString());
            add(Mime.XLSX.toString());
            add(Mime.PPT1.toString());
            add(Mime.PPT2.toString());
            add(Mime.PPTX.toString());
            add(Mime.PDF.toString());
        }});
        put("txt", new ArrayList<String>() {{
            add(Mime.TXT.toString());
        }});
        put("compress", new ArrayList<String>() {{
            add(Mime.ZIP1.toString());
            add(Mime.ZIP2.toString());
            add(Mime.ZIP3.toString());
            add(Mime.GZIP.toString());
            add(Mime.SEVENZ1.toString());
            add(Mime.SEVENZ2.toString());
            add(Mime.RAR1.toString());
            add(Mime.RAR2.toString());
        }});
        put("image", new ArrayList<String>() {{
            add(Mime.GIF.toString());
            add(Mime.JPG1.toString());
            add(Mime.JPG2.toString());
            add(Mime.PNG.toString());
            add(Mime.BMP1.toString());
            add(Mime.BMP2.toString());
        }});
        put("audio", new ArrayList<String>() {{
            add(Mime.MP3.toString());
            add(Mime.WAV1.toString());
            add(Mime.WAV2.toString());
            add(Mime.WMA.toString());
        }});
        put("video", new ArrayList<String>() {{
            add(Mime.MP4.toString());
            add(Mime.MOV.toString());
            add(Mime.MOVIE.toString());
            add(Mime.WEBM.toString());
            add(Mime.RM.toString());
            add(Mime.RMVB.toString());
            add(Mime.AVI1.toString());
            add(Mime.AVI2.toString());
            add(Mime.AVI3.toString());
        }});
    }};

    public final List<String> TYPE_OFFICE = types.get("office");
    public final List<String> TYPE_TXT = types.get("txt");
    public final List<String> TYPE_COMPRESS = types.get("compress");
    public final List<String> TYPE_IMAGE = types.get("image");
    public final List<String> TYPE_AUDIO = types.get("audio");
    public final List<String> TYPE_VIDEO = types.get("video");

    public enum Mime {
        HTML1("text/html"),
        HTML2("application/html"),
        XML1("text/xml"),
        XML2("application/xml"),
        TXT("text/plain"),
        JS1("application/javascript"),
        JS2("application/x-javascript"),
        CSS("text/css"),

        DOC("application/msword"),
        DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        XLS1("application/x-xls"),
        XLS2("application/vnd.ms-excel"),
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        PPT1("application/x-ppt"),
        PPT2("application/vnd.ms-powerpoint"),
        PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
        PDF("application/pdf"),

        ZIP1("application/zip"),
        ZIP2("application/x-zip-compressed"),
        ZIP3("application/x-compressed-zip"),
        GZIP("application/gzip"),
        SEVENZ1("application/x-7z-compressed"),
        SEVENZ2("application/octet-stream"),
        RAR1("application/rar"),
        RAR2("application/x-rar-compressed"),

        GIF("image/gif"),
        JPG1("image/jpeg"),
        JPG2("image/pjpeg"),
        PNG("image/png"),
        BMP1("application/x-bmp"),
        BMP2("image/bmp"),

        MP3("audio/mp3"),
        WAV1("audio/wav"),
        WAV2("audio/x-wav"),
        WMA("audio/x-ms-wma"),

        MP4("video/mpeg4"),
        MOV("video/quicktime"),
        AVI1("video/avi"),
        AVI2("video/x-msvideo"),
        AVI3("video/msvideo"),
        MOVIE("video/x-sgi-movie"),
        WEBM("audio/webm"),
        RM("audio/x-pn-realaudio"),
        RMVB("application/vnd.rn-realmedia-vbr");

        private String flag;

        Mime(String flag) {
            this.flag = flag;
        }

        @Override
        public String toString() {
            return this.flag;
        }
    }

}
