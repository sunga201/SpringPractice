package com.example.study.service;

import com.example.study.model.entity.Partner;
import com.example.study.model.enumClass.PartnerStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.PartnerApiRequest;
import com.example.study.model.network.response.PartnerApiResponse;
import com.example.study.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PartnerApiLogicService extends BaseService<PartnerApiRequest, PartnerApiResponse, Partner> {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Header<PartnerApiResponse> create(Header<PartnerApiRequest> request) {
        PartnerApiRequest body=request.getData();

        Partner partner=Partner.builder()
                .name(body.getName())
                .status(PartnerStatus.REGISTERED)
                .address(body.getAddress())
                .callCenter(body.getCallCenter())
                .partnerNumber(body.getPartnerNumber())
                .businessNumber(body.getBusinessNumber())
                .ceoName(body.getCeoName())
                .registeredAt(LocalDateTime.now())
                .category(categoryRepository.getOne(body.getCategoryId()))
                .build();

        Partner newPartner = baseRepository.save(partner);
        return getResponse(newPartner);
    }

    @Override
    public Header<PartnerApiResponse> read(Long id) {
        Optional<Partner> opt = baseRepository.findById(id);
        return opt
                .map(partner->getResponse(partner))
                .orElseGet(()->Header.ERROR("데이터 없음."));
    }

    @Override
    public Header<PartnerApiResponse> update(Header<PartnerApiRequest> request) {
        PartnerApiRequest body = request.getData();
        Long id = body.getId();
        Optional<Partner> opt = baseRepository.findById(id);

        return opt
                .map(partner->{
                    partner
                            .setName(body.getName())
                            .setStatus(body.getStatus())
                            .setAddress(body.getAddress())
                            .setCallCenter(body.getCallCenter())
                            .setPartnerNumber(body.getPartnerNumber())
                            .setBusinessNumber(body.getBusinessNumber())
                            .setCeoName(body.getCeoName())
                            .setCategory(categoryRepository.getOne(body.getCategoryId()))
                            ;

                    if(body.getStatus().equals(PartnerStatus.UNREGISTERED)){
                        partner.setUnregisteredAt(LocalDateTime.now());
                    }

                    Partner newPartner = baseRepository.save(partner);
                    return newPartner;
                })
                .map(partner->baseRepository.save(partner))
                .map(partner->getResponse(partner))
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        Optional<Partner> opt = baseRepository.findById(id);
        return opt
                .map(partner->{
                  baseRepository.delete(partner);
                  return Header.OK();
                })
                .orElseGet(()->Header.ERROR("데이터 없음."));
    }

    public Header<PartnerApiResponse> getResponse(Partner partner){
        PartnerApiResponse res = PartnerApiResponse.builder()
                .id(partner.getId())
                .name(partner.getName())
                .status(partner.getStatus())
                .address(partner.getAddress())
                .callCenter(partner.getCallCenter())
                .partnerNumber(partner.getPartnerNumber())
                .businessNumber(partner.getBusinessNumber())
                .ceoName(partner.getCeoName())
                .registeredAt(partner.getRegisteredAt())
                .categoryId(partner.getCategory().getId())
                .build();
        return Header.OK(res);
    }
}
