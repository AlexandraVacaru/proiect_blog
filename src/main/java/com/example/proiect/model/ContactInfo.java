package com.example.proiect.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "contact_info")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_info_id")
    private Long contactInfoId;

    @NotEmpty(message = "PhoneNumber cannot be empty!")
    @Column(unique = true)
    private String phoneNumber;

    @NotEmpty(message = "City cannot be empty!")
    private String city;

    @NotEmpty(message = "Country cannot be empty!")
    private String country;

    @OneToOne(mappedBy = "contactInfo",  cascade = CascadeType.ALL)
    private User user;

    public void removeUser(User user) {
        user.setContactInfo(null);
        this.user = null;
    }
}
