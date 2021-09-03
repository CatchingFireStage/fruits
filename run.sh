#!/bin/bash

#设置mysql的账号密码
export FRUITS_MYSQL_USERNAME=xxxx1
export FRUITS_MYSQL_PASSWORD=xxxx2

#设置后台的系统账号
export FRUITS_USERNAME=xxxx3
export FRUITS_PASSWORD=xxxx4

#项目文件的访问域名
export UPLOAD_VISIT_DOMAIN=xxxx5


#列出当前所有的环境变量
export -p

sudo docker-compose -f ./prod-docker-compose.yml down


sudo docker-compose -f ./prod-docker-compose.yml up -d