# Dataservices java application builder
#
FROM tomcat:9.0.30-jdk11

# copy the application from the bin folder to the containers webapp directory
COPY target/db-services2.war /usr/local/tomcat/webapps/_ds.war

# Expose our ports
EXPOSE 8080

# THE FIRST TIME; TO GET THE INITIAL INSTALLED CONF DIR
# mkdir -p all # use to see everything for copying stuff from
# mkdir -p conf 
# mkdir -p conf/conf.d
# mkdir -p conf/certs
# mkdir -p content
# docker create -ti --name dummy nginx bash
# docker cp dummy:/etc/nginx/ ./all/
# docker cp dummy:/etc/nginx/nginx.conf ./conf/
# docker cp dummy:/etc/nginx/conf.d/default.conf ./conf/conf.d/default.conf
# docker rm -fv dummy
# NOTE: manually move anything else you want to alter from all into the correct location

# TO GET DEBIAN SSL CERT (SELF SIGNED)
# docker run -it debian (to start a debian instance in interactive bash mode)
# apt-get udpate
# apt-get install openssl
# openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout nginx.key -out nginx.crt (places in root folder)
# NOTE:  question values: US -> TX -> Richardson -> <Enter> -> DevOps -> localhost -> <default>
# [in a new terminal window] cd to the certs folder
# docker ps (to find the name like "romantic_perlman" which will be based on debian image)
# docker cp romantic_perlman:/nginx.crt .
# docker cp romantic_perlman:/nginx.key .

# TO BUILD
# docker image rm db-services
# docker build -t db-services:t9jdk11v6 .
# docker tag db-services:t9jdk11v6 db-services

# TO TEST
# docker run -it --name db-services -p 8080:8080 -v db-services-data:/root/data/dbServices db-services

# TO PUSH TO REPO
# docker tag db-services sasonline/db-services
# docker login
# docker push sasonline/db-services
# docker push sasonline/db-services:t9jdk11v2
# docker tag db-services sasonline/db-services:t9jdk11v6

# OLD STUFF LEFT OVER FOR EXAMPLES
# docker run -it --volumes-from db-services --name datamgr busybox
# docker cp -L datamgr:/data ./data/
# docker rm backupdata

# TO REMOVE ALL STOPPED CONTAINERS FOR CLEANUP
# docker rm $(docker ps -a -q)
# TO REMOVE WITH volumes add -v

# TO REMOVE ALL ORPHANED volumes
# docker volume ls -qf dangling=true
# docker volume rm $(docker volume ls -qf dangling=true)
# better yet docker volume prune

# TO BACKUP ON HOST
# mkdir 2020-01-04
# docker cp -L db-services:/root/data/dbServices 2020-01-04/
# todo: create a backupscript to set date and read different containers and locations

# ON PRODUCTION
# docker run -it --name db-services -p 8888:8080 -v db-services-data:/root/data/dbServices sasonline/db-services
