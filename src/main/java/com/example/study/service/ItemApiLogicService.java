package com.example.study.service;

import com.example.study.model.entity.Item;
import com.example.study.model.enumClass.ItemStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.ItemApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ItemApiLogicService extends BaseService<ItemApiRequest, ItemApiResponse, Item> {

    @Autowired
    private PartnerRepository partnerRepository;

    @Override
    public Header<ItemApiResponse> create(Header<ItemApiRequest> request) {
        ItemApiRequest body = request.getData();
        Item item= Item.builder()
                .status(ItemStatus.REGISTERED)
                .name(body.getName())
                .title(body.getTitle())
                .content(body.getContent())
                .price(body.getPrice())
                .brandName(body.getBrandName())
                .registeredAt(LocalDateTime.now())
                .partner(partnerRepository.getOne(body.getPartnerId()))
                .build();

        Item newItem=baseRepository.save(item);
        return getResponse(newItem);
    }

    @Override
    public Header<ItemApiResponse> read(Long id) {
        Optional<Item> opt=baseRepository.findById(id);
        return opt.map(item->getResponse(item))
                .orElseGet(()->Header.ERROR("데이터 없음."));
    }

    @Override
    public Header<ItemApiResponse> update(Header<ItemApiRequest> request) {
        ItemApiRequest body = request.getData();

        // 업데이트할 데이터 찾기
        Optional<Item> opt=baseRepository.findById(body.getId());

        // update
        return opt.map(item->{
            item.setStatus(body.getStatus())
                    .setName(body.getName())
                    .setTitle(body.getTitle())
                    .setContent(body.getContent())
                    .setPrice(body.getPrice())
                    .setBrandName(body.getBrandName());
            if(body.getStatus().equals(ItemStatus.UNREGISTERED)) // status가 UNREGISTERED로 변하면
                item.setUnregisteredAt(LocalDateTime.now());     // unregisteredAt 필드 업데이트
            return item;
        })
                .map(item -> baseRepository.save(item))
                .map(item -> getResponse(item))
                .orElseGet(()->Header.ERROR("데이터 없음."));
    }

    @Override
    public Header delete(Long id) {
        Optional<Item> opt = baseRepository.findById(id);
        return opt.map(item->{
            baseRepository.delete(item);
            return Header.OK();
        }).orElseGet(()->Header.ERROR("데이터 없음."));
    }

    private Header<ItemApiResponse> getResponse(Item item){

        ItemApiResponse body= ItemApiResponse.builder()
                .id(item.getId())
                .status(item.getStatus())
                .name(item.getName())
                .title(item.getTitle())
                .content(item.getContent())
                .price(item.getPrice())
                .brandName(item.getBrandName())
                .registeredAt(item.getRegisteredAt())
                .partnerId(item.getPartner().getId())
                .build();

        return Header.OK(body);
    }
}
