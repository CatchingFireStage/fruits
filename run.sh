#!/bin/bash

#设置mysql的账号密码
export FRUITS_MYSQL_USERNAME=xxxx1
export FRUITS_MYSQL_PASSWORD=xxxx2

#设置后台的系统账号
export FRUITS_USERNAME=xxxx3
export FRUITS_PASSWORD=xxxx4

#项目文件的访问域名
export UPLOAD_VISIT_DOMAIN=xxxx5


#小程序配置
export FRUITS_MINIAPP_APPID=xxxx1
export FRUITS_MINIAPP_SECRET=xxxx1

#微信支付配置
export FRUITS_WECHAT_MCH_ID=xxxx1
export FRUITS_WECHAT_APIV3_KEY=xxxx1

#项目域名
export FRUITS_DOMAIN=xxxx1


#列出当前所有的环境变量
export -p


#sudo -E的作用： 当前run.sh是普通用户执行的，所以上面export设置的环境变量，在执行 sudo 的命令前，会被重置到root用户才有的环境变量
#最终导致，sudo命令中需要依赖的环境变量失效
#加上sudo -E ， 意思是，执行当前的sudo命令，会引用当前用户所export的环境变量

sudo -E docker-compose -f ./prod-docker-compose.yml down


sudo -E docker-compose -f ./prod-docker-compose.yml up -d