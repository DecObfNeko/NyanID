# NyanID [UserServer] API 使用文档

------

- ~~*小鳥遊ホシノ不懂乱写的,可能会有一些错误*~~
- ***Powered by DecObfNeko Development Team***
- 错误信息的返回格式可以查看这里-here
- 前端尽量使用Vue3创建项目(PHP,Flask也可以,但不建议)

------

## ***-网站API***

> 我们规定网站API开头为***api/zako/v1***

### 用户注册登录

#### Register API

- 请求地址:*"/reg"*

- 请求模式：***POST***

- 请求体:

  ```json
  {
      "uname":"用户名",
      "pwd":"密码",
      "e":"邮箱",
      "p":"用户IP" //php可直接获取
  }
  ```

  我们注意这里需要返回用户真实IP地址,为防止用户使用代理,我们应该检查这些请求头:

  ```
  X-Forwarded-For
  Proxy-Client-IP
  WL-Proxy-Client-IP
  HTTP_CLIENT_IP
  HTTP_X_FORWARDED_FOR
  ```

如果存在这些请求头,我们需要检查是否为本地(即内网IP段,如127.0.01),如果是则忽略(即忽略这些请求头,直接获取用户的IP),不是则取这些头中的IP

***!!注意,如果请求的Json中不包含"p",则会直接返回错误并拉黑该请求的IP地址10分钟***~~*<u>让我看看是哪个小朋友乱请求</u>*~~

返回:

```json
{
    "u":true
}
```

只有返回Ture一种可能(其他就是报错了),这时候提示用户查看自己的邮箱

REGISTER END

#### Login API

- 请求地址:*"ddu/bca/lue"*

- 请求模式:POST

- 请求头: ILik4H0shiN0 : Loser

- 请求体:

  ```json
  {
      "e":"电子邮箱",
      "pwd":"密码",
      "uap":true/false
      
  }
  ```

  注意!***uap***为是否使用Authenticator登录(让用户选择)

  ***必须带请求头!!!!***

  返回:

  1. 情况1(登录成功)

     ```json
     {
         "y":true,
         "d":"JWT"
     }
     ```

     把d存入用户的cookie中,***登录结束喵***~

  2. 情况2(账号被害死(BAN))

     返回Error为***Dimples1337***,提示用户账号有可能被BAN

  3. 情况3(密码或邮箱错误)

     返回Error为***NotFoundAccount***,提示用户账号邮箱或密码错误

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