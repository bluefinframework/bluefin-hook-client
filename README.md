[![Build Status](https://travis-ci.org/bluefinframework/bluefin-hook-client.svg?branch=master)](https://travis-ci.org/bluefinframework/bluefin-hook-client)

> Bluefin Hook Client 是 Bluefin Server的Hook 的客户端,以下简称为client.
目的是对接Bluefin Server 的hook服务,将Bluefin Server中的hook信息以邮件形式发送出去.

## 配置

Bluefin Hook Client 唯一需要配置的就是邮件模板信息,该模板正文为固定格式的json.结构如下:

```
{
    "serverinfo":{
        "host":"",
        "port":0,
        "username":"",
        "password":"",
        "encoding":""
    },
    "mail":{
        "from":"",
        "to":[

        ],
        "cc":[

        ],
        "bcc":[

        ],
        "subject":"",
        "body":{
            "text":"",
            "html":""
        }
    }
}
```

其中,serverinfo配置邮件发送人信息,mail配置邮件内容,client会对`subject`, `text`, `html`中的某些关键字做替换,来达到个性化需求.关键字如下:

```
$PACKAGE_NAME  -- package名称
$UPDATE_INFO  -- 更新信息
$MIN_VERSION  --  最小版本号
$DOWNLOAD_URL --  apk下载链接
$UPDATE_TIME  --  上传时间
$FILE_MD5  -- apk文件的MD5
$VERSION_NAME  -- 版本名称
$VERSION_CODE  -- 版本号
$SIZE  -- apk文件大小
$EXT_DATA  -- apk附加信息
$IDENTITY  -- apk唯一标识
$NAME  -- apk名称
$ICON_URL  -- icon图标链接
```

如上,配置好上述模板后,通过如下两种方式之一来指定模板文件的路径:

* 修改application.properties 中的参数:mail.template.path

* 添加系统变量:BLUEFIN_TEMPLATE_PATH

## 启动

client可以通过两种方式启动:

*  maven

```
mvn  spring-boot:run
```

* Docker

```
docker run -it -p 2557:2557 -v $(pwd):/root saymagic/bluefin-hook:v1.0.0
```


## Licence

[gpl-3.0](https://opensource.org/licenses/gpl-3.0.html)