package com.smtl.edi.util;

/**
 *
 * @author nm
 */
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.log4j.Logger;

public class MailUtil {

    final static Logger LOGGER = Logger.getLogger(MailUtil.class);

    //发件人地址
    public static String senderAddress = PropertiesUtil.getValue("mail.from");
    //发件人账户名
    public static String senderUsername = PropertiesUtil.getValue("mail.username");
    //发件人账户密码
    public static String senderPassword = PropertiesUtil.getValue("mail.password");

    //1、连接邮件服务器的参数配置
    public final static Properties PROPS = new Properties();
    public static boolean debug = false;

    static {
        //设置用户的认证方式
        PROPS.setProperty("mail.smtp.auth", PropertiesUtil.getValue("mail.smtp.auth"));
        //设置传输协议
        PROPS.setProperty("mail.transport.protocol", "smtp");
        //设置发件人的SMTP服务器地址
        PROPS.setProperty("mail.smtp.host", PropertiesUtil.getValue("mail.host"));
    }

    public static void main(String[] args) throws Exception {

        //sendPlainText("QQ邮箱激活", "martin.cui@suzhouterminals.com", "QQ邮箱激活");
        sendTxtAttachment("D:\\TG_EDI\\CMA\\20190610\\CNTAGDTCT-CMA-CODECO-INVENTORY-1560149376781.JSON", "QQ邮箱激活", "martin.cui@suzhouterminals.com");
    }

    /**
     * 发送纯文本
     *
     * @param subject
     * @param to
     * @param body
     * @param cc
     */
    public static void sendPlainText(String subject, String to, String body, String... cc) {
        try {
            //2、创建定义整个应用程序所需的环境信息的 Session 对象
            Session session = Session.getInstance(PROPS);
            //设置调试信息在控制台打印出来
            session.setDebug(debug);
            //3、创建邮件的实例对象
            //Message msg = getTextImageMimeMessage(session, pic, txt);
            Message msg = getPlainTextMimeMessage(session, subject, body, to, cc);
            //设置发件人的账户名和密码
            try ( //4、根据session对象获取邮件传输对象Transport
                     Transport transport = session.getTransport()) {
                //设置发件人的账户名和密码
                transport.connect(senderUsername, senderPassword);
                //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
                transport.sendMessage(msg, msg.getAllRecipients());
                //5、关闭邮件连接
                transport.close();
            }
        } catch (MessagingException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }
    }

    /**
     * 发送文本附件
     *
     * @param txt
     * @param subject
     * @param to
     * @param cc
     */
    public static void sendTxtAttachment(String txt, String subject, String to, String... cc) {
        try {
            //2、创建定义整个应用程序所需的环境信息的 Session 对象
            Session session = Session.getInstance(PROPS);
            //设置调试信息在控制台打印出来
            session.setDebug(debug);
            //3、创建邮件的实例对象
            //Message msg = getTextImageMimeMessage(session, pic, txt);
            Message msg = getTextMimeMessage(session, subject, txt, to, cc);
            //设置发件人的账户名和密码
            try ( //4、根据session对象获取邮件传输对象Transport
                     Transport transport = session.getTransport()) {
                //设置发件人的账户名和密码
                transport.connect(senderUsername, senderPassword);
                //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
                transport.sendMessage(msg, msg.getAllRecipients());
                //5、关闭邮件连接
                transport.close();
            }
        } catch (MessagingException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }
    }

    /**
     * 纯文本
     *
     * @param session
     * @param subject
     * @param body
     * @param to
     * @param cc
     * @return
     */
    public static MimeMessage getPlainTextMimeMessage(Session session, String subject, String body, String to, String... cc) {

        MimeMessage msg = null;

        try {
            //1.创建一封邮件的实例对象
            msg = new MimeMessage(session);
            //2.设置发件人地址
            msg.setFrom(new InternetAddress(senderAddress));
            /**
             * 3.设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
             * MimeMessage.RecipientType.TO:发送 MimeMessage.RecipientType.CC：抄送
             * MimeMessage.RecipientType.BCC：密送
             */
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            if (null != cc && cc.length > 0) {
                for (int i = 0; i < cc.length; i++) {
                    msg.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(cc[i]));
                }
            }
            //4.设置邮件主题
            msg.setSubject(subject, "UTF-8");

            //下面是设置邮件正文
            msg.setContent(body, "text/html;charset=UTF-8");

            //设置邮件的发送时间,默认立即发送
            msg.setSentDate(new Date());

        } catch (MessagingException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }
        return msg;
    }

    /**
     * 获得创建一封邮件的实例对象（图片+文本）
     *
     * @param session
     * @param subject
     * @param picture
     * @param txt
     * @param to
     * @param cc
     * @return
     */
    public static MimeMessage getTextImageMimeMessage(Session session, String subject, String picture, String txt, String to, String... cc) {

        MimeMessage msg = null;

        try {
            //1.创建一封邮件的实例对象
            msg = new MimeMessage(session);
            //2.设置发件人地址
            msg.setFrom(new InternetAddress(senderAddress));
            /**
             * 3.设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
             * MimeMessage.RecipientType.TO:发送 MimeMessage.RecipientType.CC：抄送
             * MimeMessage.RecipientType.BCC：密送
             */
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            if (null != cc && cc.length > 0) {
                for (int i = 0; i < cc.length; i++) {
                    msg.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(cc[i]));
                }
            }
            //4.设置邮件主题
            msg.setSubject(subject, "UTF-8");

            //下面是设置邮件正文
            //msg.setContent("简单的纯文本邮件！", "text/html;charset=UTF-8");
            // 5. 创建图片"节点"
            MimeBodyPart image = new MimeBodyPart();
            // 读取本地文件
            DataHandler dh = new DataHandler(new FileDataSource(picture));
            // 将图片数据添加到"节点"
            image.setDataHandler(dh);
            // 为"节点"设置一个唯一编号（在文本"节点"将引用该ID）
            String mills = String.valueOf(System.currentTimeMillis());
            image.setContentID(mills);

            // 6. 创建文本"节点"
            MimeBodyPart text = new MimeBodyPart();
            // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
            text.setContent("<a href='#'><img src='cid:" + mills + "'/></a>", "text/html;charset=UTF-8");

            // 7. （文本+图片）设置 文本 和 图片"节点"的关系（将 文本 和 图片"节点"合成一个混合"节点"）
            MimeMultipart mm_text_image = new MimeMultipart();
            mm_text_image.addBodyPart(text);
            mm_text_image.addBodyPart(image);
            mm_text_image.setSubType("related");    // 关联关系

            // 8. 将 文本+图片 的混合"节点"封装成一个普通"节点"
            // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
            // 上面的 mailTestPic 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
            MimeBodyPart text_image = new MimeBodyPart();
            text_image.setContent(mm_text_image);

            // 9. 创建附件"节点"
            MimeBodyPart attachment = new MimeBodyPart();
            // 读取本地文件
            DataHandler dh2 = new DataHandler(new FileDataSource(txt));
            // 将附件数据添加到"节点"
            attachment.setDataHandler(dh2);
            // 设置附件的文件名（需要编码）
            attachment.setFileName(MimeUtility.encodeText(dh2.getName()));

            // 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(text_image);
            mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
            mm.setSubType("mixed");         // 混合关系

            // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
            msg.setContent(mm);
            //设置邮件的发送时间,默认立即发送
            msg.setSentDate(new Date());

        } catch (MessagingException | UnsupportedEncodingException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }

        return msg;
    }

    /**
     * 获得创建一封邮件的实例对象（文本）
     *
     * @param session
     * @param subject
     * @param txt
     * @param to
     * @param cc
     * @return
     */
    public static MimeMessage getTextMimeMessage(Session session, String subject, String txt, String to, String[] cc) {

        MimeMessage msg = null;

        try {
            //1.创建一封邮件的实例对象
            msg = new MimeMessage(session);
            //2.设置发件人地址
            msg.setFrom(new InternetAddress(senderAddress));
            /**
             * 3.设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
             * MimeMessage.RecipientType.TO:发送 MimeMessage.RecipientType.CC：抄送
             * MimeMessage.RecipientType.BCC：密送
             */
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            if (null != cc && cc.length > 0) {
                for (int i = 0; i < cc.length; i++) {
                    msg.addRecipient(MimeMessage.RecipientType.CC, new InternetAddress(cc[i]));
                }
            }
            //4.设置邮件主题
            msg.setSubject(subject, "UTF-8");

            //下面是设置邮件正文
            //msg.setContent("简单的纯文本邮件！", "text/html;charset=UTF-8");
            // 5. 创建附件"节点"
            MimeBodyPart attachment = new MimeBodyPart();
            // 读取本地文件
            DataHandler dh = new DataHandler(new FileDataSource(txt));
            // 将附件数据添加到"节点"
            attachment.setDataHandler(dh);
            // 设置附件的文件名（需要编码）
            attachment.setFileName(MimeUtility.encodeText(dh.getName()));

            // 6. 设置（文本）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
            MimeMultipart mm = new MimeMultipart();
            mm.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
            mm.setSubType("mixed");         // 混合关系

            // 7. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
            msg.setContent(mm);
            //设置邮件的发送时间,默认立即发送
            msg.setSentDate(new Date());

        } catch (AddressException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        } catch (MessagingException | UnsupportedEncodingException ex) {
            LOGGER.error(ExceptionUtil.getStackTraceAsString(ex));
        }
        return msg;
    }
}
