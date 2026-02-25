package com.kiloux.restopos.model;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String membershipTier;
    private int points;

    public Customer() {}

    public Customer(String name, String phone, String membershipTier, int points) {
        this.name = name;
        this.phone = phone;
        this.membershipTier = membershipTier;
        this.points = points;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMembershipTier() { return membershipTier; }
    public void setMembershipTier(String membershipTier) { this.membershipTier = membershipTier; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
    
    @Override
    public String toString() {
        return name + " (" + membershipTier + ")";
    }
}
