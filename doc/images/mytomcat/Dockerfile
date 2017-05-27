FROM tomcat
MAINTAINER "Zhiwen Zhang <zhangzhiwen91@gmail.com>"
RUN \
  mkdir -p /usr/local/tomcat/conf

ADD \
  settings.xml 		/usr/local/tomcat/conf/

ADD \
  context.xml 	/usr/local/tomcat/webapps/manager/META-INF
  
ADD \
  tomcat-users.xml 	/usr/local/tomcat/conf/


  