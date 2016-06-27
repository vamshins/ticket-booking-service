package com.walmart.ticket.common.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by Vamshi on 6/27/2016.
 */
public class ClientError {

    private String title;
    private String message;
    private String path;

    public ClientError(String title, String message, String path) {
        this.title = title;
        this.message = message;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ClientError that = (ClientError) o;

        return new EqualsBuilder()
                .append(title, that.title)
                .append(message, that.message)
                .append(path, that.path)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(title)
                .append(message)
                .append(path)
                .toHashCode();
    }
}
