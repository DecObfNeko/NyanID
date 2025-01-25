# NyanID [UserServer] API 使用文档

------

- ApiDocs Version：1.0
- AwA

------

## *网站API*

> 我们规定私有API开头为***api/zako/v1*/********
>
> 我们规定公共API开头为**api/zako/v2/********
>
> 我们规定静态资源(公共API)API开头为**api/zako/res/********

### -用户注册登录(私有API)



#### Register API

- 请求地址:***register***

- 请求模式：***POST***

- 请求体:

  ```json
  {
      "uname" : "用户名",
      "pwd" : "电子邮箱",
      "e" : "电子邮箱"
  }
  ```
  
  后端会检查这些请求头:
  
  ```
  X-Forwarded-For
  Proxy-Client-IP
  WL-Proxy-Client-IP
  HTTP_CLIENT_IP
  HTTP_X_FORWARDED_FOR
  ```

​	如果存在这些请求头,我们需要检查是否为本地(即内网IP段,如127.0.01),如果是则忽略(即忽略这些请求头,直接获取用户的IP),不是则	取这些头中的IP

- 返回:

  ```json
  {
      "status": 200,
      "message": "请前往邮箱验证然后完成注册,注意,链接有效期只有5分钟,请尽快验证喵!",
      "timestamp": "2025-01-25T14:28:18.613558867"
  }
  ```

- 如果注册失败返回

  ```json
  {
      "status": 403,
      "error": "Illegal Request",
      "message": "错误信息",
      "timestamp": "2025-01-25T14:34:42.269240459"
  }
  ```

​	**REGISTER END**





#### 邮箱验证API

- 请求地址:***verification***

- 请求模式：***POST***

- 请求体:

  ```json
  {
      "code":"TOKEN"
  }
  ```


从发送到用户邮箱的链接,列如:http://nyanid.cn:8080/verification/rTy4uAjdCdbO3ZVQDjRpkHcFQFQqkaRKIMVxCywrIU4Rz2ihKYYZHcRbSBmTUBWZtBzxaTgfjkxYKEnnxGEhlj5vRycPJtVo3ayba4sbxrB6F76zvFU51LA3ZqCjsD6M

其中 "rTy4uAjdCdbO3ZVQDjRpkHcFQFQqkaRKIMVxCywrIU4Rz2ihKYYZHcRbSBmTUBWZtBzxaTgfjkxYKEnnxGEhlj5vRycPJtVo3ayba4sbxrB6F76zvFU51LA3ZqCjsD6M"便是TOKEN

- 返回:

  ```json
  {
      "status": 200,
      "message": "The verification is successful, please go to Login 杂鱼喵~",
      "timestamp": "2025-01-25T14:32:46.479760059"
  }
  ```

- 如果验证失败:

  ```json
  {
      "status": 403,
      "error": "Illegal Request",
      "message": "The verification code is incorrect or invalid 杂鱼喵~",
      "timestamp": "2025-01-25T14:43:23.579334549"
  }
  ```

  **VerificationEND**

  

  

#### Login API

- 请求地址:***login***

- 请求模式:**POST**

- 请求体:

  ```json
  {
      "email" : "neko@nyacat.cloud", //邮箱
      "pwd" : "pwd", //密码
      "devid" : "deviceID", //设备ID
      "devname" : "deviceName" //设备名字
  }
  ```

其中**devname**和**devid**在**网站登录**的情况下**可以不带**,如果是**网站登录需要请求头**: 

```
LoginForWeb : true
```

1. 返回:

   登录成功:

   ```json
   {
       "data": "Base64编码后的ClientID",
       "status": "success",
       "token": "认证TOKEN",
       "timestamp": "2025-01-25T14:55:25.997043834"
   }
   ```

   在网站登录成功时,会返回一个JSESSIONID Cookie,这个Cookie是标识当前设备使用的,如果在登录时候带上JSESSIONID这个Cookie服务端会返回与其对应的设备的Token和ClientID,如果在登录时不带上这个Cookie则服务端会将这个请求视为一个新的设备登录

   

   登录成功后把Token和Base64编码的ClientID以你喜欢的方法保存

   

2. 其他情况均为登录失败

   

**LoginEnd**

​	

### -用户数据的访问与修改(私有)





### -服务器数据获取(公共)



#### 静态资源获取:

- 请求地址:**{type}/{data}**

- 请求模式:**GET**

- TYPE:资源类型,**avatar(用户头像),img(图片资源),config(配置)**

- DATA:资源UID,**avatar(用户UID),img(图片UID),config(配置ID)**

- 返回:

  对应资源





#### 服务器信息获取:

- 请求地址:**server**

- 请求模式:**GET**

- 返回:

  ```json
  {
      "AllUser": "1", //总注册用户
      "AllApplication": "0",//注册应用
      "NumberOfEvents": 0 //事件队列数
  }
  ```

  











## ***-错误信息***

| 错误类型         | 解释                    |
| ---------------- | :---------------------- |
| IllegalClient    | Illegal Client Request  |
| IllegalRequest   | Illegal Request         |
| UNKNOWN_ERROR    | Unknown Error           |
| NotFoundAccount  | Not Found Account       |
| MethodNotAllowed | Method Not Allowed      |
| Unauthorized     | Unauthorized            |
| Dimples1337      | Don't Hacking,Fuck You! |

**JSON格式数据**:

```json
{
    "status": 403,
    "error": "Illegal Client Request",
    "message": "b8cd2eca-eecf-38b2-8331-92e226d7cc61",
    "timestamp": "2024-08-30T03:17:08.068206205"
}
```

- status:HTTP状态码
- error:错误类型
- message:消息,返回给用户
- timestamp:时间