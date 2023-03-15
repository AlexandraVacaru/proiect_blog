package com.example.proiect.service;

import com.example.proiect.model.ContactInfo;

public interface ContactInfoService {
    ContactInfo save(ContactInfo contactInfo);

    void deleteContactInfo(Long contactInfoId);

    ContactInfo findById(Long contactInfoId);
}
