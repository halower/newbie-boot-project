package com.newbie.deploy

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @Author: halower
 * @Date: 2019/6/11 20:32
 *
 */
class CustomDeployPlugin implements Plugin<Project> {
    def devSuffix = "-dev"
    def applicationNameKey = "spring.application.name"
    def dubboVersionKey = "dubbo.provider.version"
    def bootstrap = new File('src/main/resources/bootstrap.properties')
    def application = new File('src/main/resources/application.properties')
    @Override
    void apply(Project project) {
        def task = project.tasks.create("联调模式")
        task.group = 'toolkit'
        task.doLast {
                def updatedBootstrap = []
                bootstrap.readLines().each {
                    def line ->
                        def isChanged = line.startsWith(applicationNameKey) && !line.endsWith(devSuffix)
                        updatedBootstrap.add(isChanged ? line+ devSuffix: line)
                }
                bootstrap.delete()
                bootstrap.createNewFile()
                bootstrap.withWriter { def writer ->
                    updatedBootstrap.each {
                        line -> writer.writeLine line
                    }
                }

                def random = new Random()
                application.withWriter { def writer ->
                    writer.writeLine "spring.profiles.active=dev"
                    writer.writeLine "dubbo.registry.register=false"
                    writer.writeLine dubboVersionKey + "=" + random.nextInt(3) + "." + random.nextInt(10)  + "." +random.nextInt(15)
                }
            }
        }
}
