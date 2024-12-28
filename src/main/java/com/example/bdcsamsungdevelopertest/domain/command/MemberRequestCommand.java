package com.example.bdcsamsungdevelopertest.domain.command;

// TODO change all related record types to this class
public class MemberRequestCommand {
    private Long id;                                    // for update uses only
    private String name;
    private String email;
    private String address;
    public MemberRequestCommand(
        String name,
        String email,
        String address
    ) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }        // for update uses only
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
