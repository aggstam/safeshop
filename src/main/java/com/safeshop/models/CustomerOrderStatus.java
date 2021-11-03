package com.safeshop.models;

public enum CustomerOrderStatus {

    PENDING("Pending"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String text;

    CustomerOrderStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
