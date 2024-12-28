package com.example.bdcsamsungdevelopertest.domain.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "member")
public class Member extends BaseTime {

    public Member(){}

    public Member(
        String name,
        String email,
        String address
    ) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    protected String name;

    @Column(name = "email", nullable = false)
    protected String email;

    @Column(name = "address", nullable = false)
    protected String address;

    /**
    * RELATIONS
    * */
    @OneToMany(
        mappedBy = "member",
        fetch = FetchType.LAZY,
        targetEntity = Orders.class,
        cascade = CascadeType.ALL
    )
    protected List<Orders> orders;

    /**
    * GETTERS
    * */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    /**
    * SETTERS
    * */
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * toString
     * */
    @Override
    public String toString() {
        return "Member{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", address='" + address + '\'' + '}';
    }
}