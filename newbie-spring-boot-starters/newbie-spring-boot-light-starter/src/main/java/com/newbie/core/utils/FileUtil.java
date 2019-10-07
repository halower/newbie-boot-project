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

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 常用文件操作
 */
public class FileUtil {

    FileUtil() {
    }

    /**
     * 根据文件路径名读取文件所有内容
     *
     * @param pathName 文件路径名
     * @param encode   编码
     * @return 文件内容
     */
    public String readAllByPathName(String pathName, String encode) throws IOException {
        return new String(Files.readAllBytes(Paths.get(pathName)), encode);
    }

    /**
     * 根据文件读取文件所有内容
     *
     * @param file   文件
     * @param encode 编码
     * @return 文件内容
     */
    public String readAllByFile(File file, String encode) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), encode);
    }

    /**
     * 根据文件路径读取文件所有内容
     *
     * @param path   文件路径
     * @param encode 编码
     * @return 文件内容
     */
    public String readAllByPath(Path path, String encode) throws IOException {
        return new String(Files.readAllBytes(path), encode);
    }

    /**
     * 根据classpath读取文件所有内容（jar包外路径优先）
     *
     * @param classpath classpath，先找jar外的文件，找不到再去读jar包内文件
     * @param encode    编码
     * @return 文件内容
     * @throws IOException
     */
    public String readAllByClassPath(String classpath, String encode) throws IOException {
        File file = new File(FileUtil.class.getResource("/").getPath() + classpath);
        if (file.exists()) {
            return readAllByFile(file, encode);
        }
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpath);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        return buffer.lines().collect(Collectors.joining("\n"));
    }

    /**
     * 从流中复制文件到磁盘，不支持目录
     *
     * @param source   流，支持jar内文件复制
     *                 e.g. Test.class.getResourceAsStream("/LICENSE-junit.txt")
     * @param destPath 磁盘路径
     * @throws IOException
     */
    public void copyStreamToPath(InputStream source, String destPath) throws IOException {
        Files.copy(source, Paths.get(destPath), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 判断是否是Windows系统
     *
     * @return 是否是Windows系统
     */
    public boolean isWindows() {
        return System.getProperty("os.name", "linux").toLowerCase().startsWith("windows");
    }


    /**
     * Glob模式文件过滤器
     *
     * @param files     要过滤的文件列表
     * @param mathRules Glob过滤规则列表
     * @return 过滤后的文件列表
     * @link https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob
     */
    public List<String> mathFilter(List<String> files, List<String> mathRules) {
        if (mathRules.isEmpty()) {
            return files;
        }
        List<PathMatcher> matchers = convertGlobMatchers(mathRules);
        return convertPaths(files)
                .filter(path -> matchers.stream()
                        .anyMatch(mather -> mather.matches(path)))
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    /**
     * 使用Glob模式过滤规则检查是否有匹配到的文件
     *
     * @param files     要匹配的文件列表
     * @param mathRules Glob过滤规则列表
     * @return 是否有匹配到的文件
     * @link https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob
     */
    public boolean anyMath(List<String> files, List<String> mathRules) {
        if (files.isEmpty()) {
            return false;
        }
        if (mathRules.isEmpty()) {
            return true;
        }
        List<PathMatcher> matchers = convertGlobMatchers(mathRules);
        return convertPaths(files)
                .anyMatch(path -> matchers.stream()
                        .anyMatch(mather -> mather.matches(path)));
    }

    /**
     * 使用Glob模式过滤规则检查是否有未匹配到的文件
     *
     * @param files     要匹配的文件列表
     * @param mathRules Glob过滤规则列表
     * @return 是否有未匹配到的文件
     * @link https://docs.oracle.com/javase/tutorial/essential/io/fileOps.html#glob
     */
    public boolean noneMath(List<String> files, List<String> mathRules) {
        if (files.isEmpty()) {
            return true;
        }
        if (mathRules.isEmpty()) {
            return false;
        }
        List<PathMatcher> matchers = convertGlobMatchers(mathRules);
        return convertPaths(files)
                .anyMatch(path -> matchers.stream()
                        .noneMatch(mather -> mather.matches(path)));
    }

    private List<PathMatcher> convertGlobMatchers(List<String> mathRules) {
        return mathRules.stream()
                .map(rule -> FileSystems.getDefault().getPathMatcher("glob:" + rule))
                .collect(Collectors.toList());
    }

    private Stream<Path> convertPaths(List<String> files) {
        return files.stream().map(path -> Paths.get(path));
    }

}
