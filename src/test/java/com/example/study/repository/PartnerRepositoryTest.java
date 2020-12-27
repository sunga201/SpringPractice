package com.example.study.repository;

import com.example.study.StudyApplicationTests;
import com.example.study.model.entity.Partner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class PartnerRepositoryTest extends StudyApplicationTests {
    @Autowired
    private PartnerRepository partnerRepository;

    @Test
    public void create(){
        String name="Partner01";
        String status="REGISTERED";
        String address="서울시 동대문구";
        String callCenter="070-295-0392";
        String partnerNumber="010-111-1111";
        String businessNumber="1234567890123";
        String ceoName="홍성현";
        LocalDateTime registeredAt=LocalDateTime.now();
        LocalDateTime createdAt=LocalDateTime.now();
        String createdBy="AdminServer";
        Long categoryId=1L;

        Partner partner=new Partner();
        partner.setName(name);
        partner.setStatus(status);
        partner.setAddress(address);
        partner.setCallCenter(callCenter);
        partner.setPartnerNumber(partnerNumber);
        partner.setBusinessNumber(businessNumber);
        partner.setCeoName(ceoName);
        partner.setRegisteredAt(registeredAt);
        partner.setCreatedAt(createdAt);
        partner.setCreatedBy(createdBy);

        Partner newPartner=partnerRepository.save(partner);

        Assertions.assertNotNull(newPartner);
    }
}
