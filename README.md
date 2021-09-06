# fruits
水果电商后台



# hibernate

[hibernate 验证使用手册](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-declaring-bean-constraints)



# 服务器免密登录ssh配置

实现功能： master 通过ssh登录到slave

```$xslt

master 192.168.35.130
slave  192.168.45.129

```

## 在master中和slave都执行

```$xslt
ssh-keygen -t rsa
#得到公私钥
```


## 把master的公钥发送给slave

```$xslt
ssh-copy-id  root@192.168.45.129
```

## 测试登录是否成功

```$xslt
#master中执行
ssh root@192.168.45.129
```




