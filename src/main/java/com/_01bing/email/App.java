package com._01bing.email;

import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.DefaultAuthenticator;


public class App 
{
    public static void main( String[] args )  throws EmailException
    {



        //读取配置文件
        // 配置文件格式：xxx.xxx.xxx=xxxx  (不要带引号）
        Properties properties = new Properties();

        try {
            FileInputStream inputStream = new FileInputStream("application.yml");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);

        }

        String ipUrl = properties.getProperty("ip.url");


        //通过访问某网站，获取本机外网IP
        System.out.println( "start get public ip ..." );

        String getIp = "";

        try {
            // 创建URL对象
            URL url = new URL(ipUrl);

            // 通过URL对象打开一个连接
            URLConnection urlConnection = url.openConnection();

            // 获取连接的输入流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            // 读取输入流的内容
            getIp = bufferedReader.readLine();

            // while ((getIp = bufferedReader.readLine()) != null) {
            //     System.out.println(getIp);
            // }

            // 关闭输入流
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    System.out.println("get ip :" + getIp);
    System.out.println("start send mail ...");


    String smtpHost = properties.getProperty("mail.smtp.host");
    int smtpPort = Integer.parseInt(properties.getProperty("mail.smtp.port"));
    String fromMail = properties.getProperty("mail.from.account");
    String fromUsername = properties.getProperty("mail.from.username");
    String fromPassword = properties.getProperty("mail.from.password");
    String toMail= properties.getProperty("mail.to.account");
    boolean sslEnable = Boolean.parseBoolean(properties.getProperty("mail.ssl.enable"));
    boolean debugEnable = Boolean.parseBoolean(properties.getProperty("mail.debug"));
    String mailCharset = properties.getProperty("mail.charset");
    String mailSubject = properties.getProperty("mail.subject");
    String mailMsg = properties.getProperty("mail.message");



    Email sendMail = new SimpleEmail();
    sendMail.setHostName(smtpHost);
    sendMail.setSmtpPort(smtpPort);
    sendMail.setAuthenticator(new DefaultAuthenticator(fromMail, fromPassword));
    sendMail.setSSLOnConnect(sslEnable);
    sendMail.setDebug(debugEnable);
    sendMail.setCharset(mailCharset);
    sendMail.setFrom(fromMail, fromUsername);
    sendMail.addTo(toMail);
    sendMail.setSubject(mailSubject + getIp);
    sendMail.setMsg(mailMsg + " ");
    sendMail.send();



    // Email sendMail = new SimpleEmail();
    // sendMail.setHostName("smtp.qq.com");
    // sendMail.setSmtpPort(465);
    // sendMail.setAuthenticator(new DefaultAuthenticator("181322642@qq.com", "asrfitzqarjmbija"));
    // sendMail.setSSLOnConnect(true);
    // sendMail.setDebug(false);
    // sendMail.setCharset("UTF-8");
    // sendMail.setFrom("181322642@qq.com", "haibing");
    // sendMail.addTo("ithaibing@qq.com");
    // sendMail.setSubject(getIp);
    // sendMail.setMsg("IP");
    // sendMail.send();



    }
}