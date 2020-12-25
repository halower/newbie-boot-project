@echo on

set version=2.0.0.A2
set newbieBootGroupId=com.github.halower
echo 开始发布依赖
call mvn deploy:deploy-file -DgroupId=%newbieBootGroupId% -DartifactId=newbie-spring-boot-management -Dversion=%version% -Dpackaging=pom -Dfile=pom.xml  -Durl=http://192.168.7.197:8081/repository/maven-releases/ -DrepositoryId=nexus-197
echo 发布成功
pause
