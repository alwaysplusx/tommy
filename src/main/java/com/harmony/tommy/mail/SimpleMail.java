package com.harmony.tommy.mail;

public class SimpleMail extends Mail {

    public SimpleMail() {
    }

    public SimpleMail(String fromAddress, String[] toAddresses, String subject, Object content) {
        super(fromAddress, toAddresses, subject, content);
    }

}
