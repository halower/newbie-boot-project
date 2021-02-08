@echo on

set version=1.0.0
set newbieBootGroupId=cn.tfswx
echo 开始发布依赖
call mvn deploy:deploy-file -DgroupId=%newbieBootGroupId% -DartifactId=newbie-spring-boot-management -Dversion=%version% -Dpackaging=pom -Dfile=pom.xml  -Durl=http://192.168.7.197:8081/repository/maven-releases/ -DrepositoryId=nexus-197
echo 发布成功
pause
