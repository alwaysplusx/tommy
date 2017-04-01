package com.harmony.tommy.mail;

public class MailTemplet {

    private String path;
    private Object[] arguments;

    public MailTemplet(String path, Object[] arguments) {
        this.path = path;
        this.arguments = arguments;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}