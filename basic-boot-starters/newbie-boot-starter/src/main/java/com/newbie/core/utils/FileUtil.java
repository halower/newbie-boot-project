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

package com.newbie.core.utils;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 常用文件操作
 */

@Deprecated
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
