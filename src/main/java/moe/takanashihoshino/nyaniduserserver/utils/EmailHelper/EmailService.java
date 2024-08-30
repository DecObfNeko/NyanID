package moe.takanashihoshino.nyaniduserserver.utils.EmailHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendVerificationCode(String user, String code) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            String htmlBody = "<!DOCTYPE html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"description\" content=\"email code\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div style=\"background-color:#ECECEC; padding: 35px;\">\n" +
                    "    <table cellpadding=\"0\" align=\"center\"\n" +
                    "           style=\"width: 800px;height: 100%; margin: 0px auto; text-align: left; position: relative; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; font-size: 14px; font-family:微软雅黑, 黑体; line-height: 1.5; box-shadow: rgb(153, 153, 153) 0px 0px 5px; border-collapse: collapse; background-position: initial initial; background-repeat: initial initial;background:#fff;\">\n" +
                    "        <tbody>\n" +
                    "        <tr>\n" +
                    "            <th valign=\"middle\"\n" +
                    "                style=\"height: 25px; line-height: 25px; padding: 15px 35px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: rgb(29, 216, 226); background-color: rgb(17, 131, 207); border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px;\">\n" +
                    "                <font face=\"微软雅黑\" size=\"5\" style=\"color: rgb(255, 255, 255); \">NyanID [猫猫通行证平台]</font>\n" +
                    "            </th>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td style=\"word-break:break-all\">\n" +
                    "                <div style=\"padding:25px 35px 40px; background-color:#fff;opacity:0.8;\">\n" +
                    "                    <h2 style=\"margin: 5px 0px; \">\n" +
                    "                        <font color=\"#333333\" style=\"line-height: 20px; \">\n" +
                    "                            <font style=\"line-height: 22px; \" size=\"4\">\n" +
                    "                                亲爱的猫猫"+user +
                    "                        </font>\n" +
                    "                    </h2>\n" +
                    "                    <p>您好喵,非常感谢您使用NyanID,我们注意到你的账户正在进行一些敏感的操作喵~所以我们需要验证你的邮箱以确保是您喵本人在操作,关注小鳥遊ホシノ喵!!!谢谢喵~\n" +
                    "                        verification code is:<font color=\"#ff8c00\">"+ code+"</font>, 杂鱼喵,这个验证码有效期为5分钟哦,请尽快使用喵!~</p>\n" +
                    "                    <div style=\"width:100%;margin:0 auto;\">\n" +
                    "                        <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n" +
                    "                            <p>DecObfNeko Development Team</p>\n" +
                    "                            <p>Contact us： neko@nyacat.cloud</p>\n" +
                    "                            <p>Please do not reply to this system email</p>\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                </div>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        </tbody>\n" +
                    "    </table>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(user);
            messageHelper.setSubject("NyanID User verified[猫猫通行证]");
            messageHelper.setText(htmlBody, true);
             messageHelper.setFrom(from);
        };
        javaMailSender.send(messagePreparator);
    }

    public void RegisterVerification(String user, String link) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            String htmlBody = "<!DOCTYPE html>\n" +
                    "<head>\n" +
                    "    <meta charset=\"utf-8\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"description\" content=\"email code\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div style=\"background-color:#ECECEC; padding: 35px;\">\n" +
                    "    <table cellpadding=\"0\" align=\"center\"\n" +
                    "           style=\"width: 800px;height: 100%; margin: 0px auto; text-align: left; position: relative; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; font-size: 14px; font-family:微软雅黑, 黑体; line-height: 1.5; box-shadow: rgb(153, 153, 153) 0px 0px 5px; border-collapse: collapse; background-position: initial initial; background-repeat: initial initial;background:#fff;\">\n" +
                    "        <tbody>\n" +
                    "        <tr>\n" +
                    "            <th valign=\"middle\"\n" +
                    "                style=\"height: 25px; line-height: 25px; padding: 15px 35px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: rgb(29, 216, 226); background-color: rgb(17, 131, 207); border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px;\">\n" +
                    "                <font face=\"微软雅黑\" size=\"5\" style=\"color: rgb(255, 255, 255); \">NyanID [猫猫通行证平台]</font>\n" +
                    "            </th>\n" +
                    "        </tr>\n" +
                    "        <tr>\n" +
                    "            <td style=\"word-break:break-all\">\n" +
                    "                <div style=\"padding:25px 35px 40px; background-color:#fff;opacity:0.8;\">\n" +
                    "                    <h2 style=\"margin: 5px 0px; \">\n" +
                    "                        <font color=\"#333333\" style=\"line-height: 20px; \">\n" +
                    "                            <font style=\"line-height: 22px; \" size=\"4\">\n" +
                    "                                亲爱的猫猫"+user +
                    "                        </font>\n" +
                    "                    </h2>\n" +
                    "                    <p>您好喵,非常感谢您注册使用NyanID,点击链接确认注册:)<a href=\""+link+"\">确认注册</a>\n" +
                    "                       <p>啥,你没注册?那就是有人用你的邮箱进行了注册操作,你只需无视这封邮件即可喵~</p>"+
                    "                    <div style=\"width:100%;margin:0 auto;\">\n" +
                    "                        <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n" +
                    "                            <p>DecObfNeko Development Team</p>\n" +
                    "                            <p>Contact us： neko@nyacat.cloud<p>\n" +
                    "                            <p>Please do not reply to this system email</p>\n" +
                    "                        </div>\n" +
                    "                    </div>\n" +
                    "                </div>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "        </tbody>\n" +
                    "    </table>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setTo(user);
            messageHelper.setSubject("NyanID User Register Verified[猫猫通行证]");
            messageHelper.setText(htmlBody, true);
            messageHelper.setFrom(from);
        };
        javaMailSender.send(messagePreparator);
    }
}