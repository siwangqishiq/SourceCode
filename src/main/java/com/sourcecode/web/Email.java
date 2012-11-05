/*
 * Copyright 2011 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.sourcecode.web;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Mail.java
 * 
 * @author baojun
 */
public class Email {

    /**
     * 是否强制给email加生产环境title
     */
    private boolean forceAddSpecialTitle = true;
    /** 收件人 **/
    private String toAddress;

    private String[] toAddresses;

    /**
     * @return the toAddresses
     */
    public String[] getToAddresses() {
        return toAddresses;
    }

    /**
     * @param toAddresses the toAddresses to set
     */
    public void setToAddresses(String[] toAddresses) {
        this.toAddresses = toAddresses;
    }

    /**
     * 发件人
     */
    private String fromAddress;

    /** 抄送给 **/
    private String cc;

    /** 邮件主题 **/
    private String subject;

    /** 邮件内容 **/
    private String content;
    /**
     * 生成时间
     */
    private Date createTime;
    /**
     * 是否HTML格式内容
     */
    private boolean isHtml = true;

    private String personal;

    /**
     * @return the personal
     */
    public String getPersonal() {
        if (StringUtils.isBlank(personal)) {
            return DEFAULT_PERSONAL;
        } else {
            return personal;
        }
    }

    /**
     * @param personal the personal to set
     */
    public void setPersonal(String personal) {
        this.personal = personal;
    }

    /**
     * 邮件默认显示名称
     */
    private final static String DEFAULT_PERSONAL = "airAD";

    /**
     * 默认发件地址
     */
    public final static String DEFAULT_FROM_ADDRESS = "contact@airad.com";

    // public final static String SUPPORT_FROM_ADDRESS = "support@airad.com";

    /**
     * @return the cc
     */
    public String getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(String cc) {
        this.cc = cc;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the isHtml
     */
    public boolean isHtml() {
        return isHtml;
    }

    /**
     * @param isHtml the isHtml to set
     */
    public void setHtml(boolean isHtml) {
        this.isHtml = isHtml;
    }

    /**
     * @return the attachment
     */
    public MultipartFile[] getAttachment() {
        return attachment;
    }

    /**
     * @param attachment the attachment to set
     */
    public void setAttachment(MultipartFile[] attachment) {
        this.attachment = attachment;
    }

    /** 附件 **/
    private MultipartFile[] attachment = new MultipartFile[0];

    /**
     * @return the toAddress
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * @param toAddress the toAddress to set
     */
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    /**
     * @return the fromAddress
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * @param fromAddress the fromAddress to set
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     */
    public Email() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param toAddress
     * @param fromAddress
     * @param subject
     * @param content
     * @param isHtml
     */
    public Email(String toAddress, String fromAddress, String subject, String content, boolean isHtml) {
        super();
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.subject = subject;
        this.content = content;
        createTime = Calendar.getInstance().getTime();
        this.isHtml = isHtml;
    }

    public Email(String[] toAddresses, String fromAddress, String subject, String content, boolean isHtml) {
        super();
        this.toAddresses = toAddresses;
        this.fromAddress = fromAddress;
        this.subject = subject;
        this.content = content;
        createTime = Calendar.getInstance().getTime();
        this.isHtml = isHtml;
    }

    /**
     * @param mailToAddress
     * @param cc2
     * @param string
     * @param subject2
     * @param fullStackTrace
     * @param b
     */
    public Email(String toAddress, String cc2, String fromAddress, String subject, String content, boolean isHtml) {
        super();
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.subject = subject;
        this.content = content;
        createTime = Calendar.getInstance().getTime();
        this.isHtml = isHtml;
        cc = cc2;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    private final static String LOG_TEMPLETE = "sendAddress: {0}, fromAddress:{1},  cc: {2} , subject:{3}, createtime:{4}, content:{5}, isHTML:{6}";

    public String toLogString() {
        return MessageFormat.format(LOG_TEMPLETE, toAddress, fromAddress, cc, subject, createTime, content, isHtml);
    }

    /**
     * @return the forceAddSpecialTitle
     */
    public boolean isForceAddSpecialTitle() {
        return forceAddSpecialTitle;
    }

    /**
     * @param forceAddSpecialTitle the forceAddSpecialTitle to set
     */
    public void setForceAddSpecialTitle(boolean forceAddSpecialTitle) {
        this.forceAddSpecialTitle = forceAddSpecialTitle;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Email [attachment=" + Arrays.toString(attachment) + ", cc=" + cc + ", content=" + content + ", createTime=" + createTime
                + ", forceAddSpecialTitle=" + forceAddSpecialTitle + ", fromAddress=" + fromAddress + ", isHtml=" + isHtml + ", personal=" + personal
                + ", subject=" + subject + ", toAddress=" + toAddress + ", toAddresses=" + Arrays.toString(toAddresses) + "]";
    }
}
