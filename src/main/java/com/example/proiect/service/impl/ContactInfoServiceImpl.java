package com.example.proiect.service.impl;

import com.example.proiect.exception.NotFoundException;
import com.example.proiect.model.ContactInfo;
import com.example.proiect.model.User;
import com.example.proiect.repository.ContactInfoRepository;
import com.example.proiect.service.ContactInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoRepository contactInfoRepository;

    @Override
    public ContactInfo save(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    @Override
    public void deleteContactInfo(Long contactInfoId) {
        Optional<ContactInfo> contactInfoOptional = contactInfoRepository.findById(contactInfoId);
        if(contactInfoOptional.isEmpty()) {
            throw new NotFoundException("Contact info not found!!");
        }
        ContactInfo contactInfo = contactInfoOptional.get();
        User user = contactInfo.getUser();
        contactInfo.removeUser(user);
        contactInfoRepository.save(contactInfo);
        contactInfoRepository.deleteById(contactInfoId);
    }

    @Override
    public ContactInfo findById(Long contactInfoId) {
        Optional<ContactInfo> contactInfoOptional = contactInfoRepository.findById(contactInfoId);
        if(contactInfoOptional.isEmpty()) {
            throw new NotFoundException("Contact info not found!!");
        }
        return contactInfoOptional.get();
    }
}
