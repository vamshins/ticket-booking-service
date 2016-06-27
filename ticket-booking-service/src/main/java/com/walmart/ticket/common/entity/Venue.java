package com.walmart.ticket.common.entity;

import java.math.BigDecimal;

/**
 * Created by Vamshi on 6/27/2016.
 *
 * Entity that holds data related to a Venue
 */
public class Venue {

    private int levelId;
    private String levelName;
    private BigDecimal price;
    private int rows;
    private int seatsInRow;

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeatsInRow() {
        return seatsInRow;
    }

    public void setSeatsInRow(int seatsInRow) {
        this.seatsInRow = seatsInRow;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "levelId=" + levelId +
                ", levelName='" + levelName + '\'' +
                ", price=" + price +
                ", rows=" + rows +
                ", seatsInRow=" + seatsInRow +
                '}';
    }
}

