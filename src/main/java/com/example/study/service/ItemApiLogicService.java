package com.example.study.service;

import com.example.study.ifs.CrudInterface;
import com.example.study.model.entity.Item;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.ItemApiRequest;
import com.example.study.model.network.response.ItemApiResponse;
import com.example.study.repository.ItemRepository;
import com.example.study.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ItemApiLogicService implements CrudInterface<ItemApiRequest, ItemApiResponse> {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Override
    public Header<ItemApiResponse> create(Header<ItemApiRequest> request) {
        ItemApiRequest body = request.getData();
        Item item= Item.builder()
                .status("REGISTERED")
                .name(body.getName())
                .title(body.getTitle())
                .content(body.getContent())
                .price(body.getPrice())
                .brandName(body.getBrandName())
                .registeredAt(LocalDateTime.now())
                .partner(partnerRepository.getOne(body.getPartnerId()))
                .build();

        Item newItem=itemRepository.save(item);
        return getResponse(newItem);
    }

    @Override
    public Header<ItemApiResponse> read(Long id) {
        Optional<Item> opt=itemRepository.findById(id);
        return opt.map(item->getResponse(item))
                .orElseGet(()->Header.ERROR("데이터 없음."));
    }

    @Override
    public Header<ItemApiResponse> update(Header<ItemApiRequest> request) {
        ItemApiRequest body = request.getData();

        // 업데이트할 데이터 찾기
        Optional<Item> opt=itemRepository.findById(body.getId());

        // update
        return opt.map(item->{
            item.setStatus(body.getStatus())
                    .setName(body.getName())
                    .setTitle(body.getTitle())
                    .setContent(body.getContent())
                    .setPrice(body.getPrice())
                    .setBrandName(body.getBrandName());

            return item;
        })
                .map(item -> itemRepository.save(item))
                .map(item -> getResponse(item))
                .orElseGet(()->Header.ERROR("데이터 없음."));
    }

    @Override
    public Header delete(Long id) {
        Optional<Item> opt = itemRepository.findById(id);
        return opt.map(item->{
            itemRepository.delete(item);
            return Header.OK();
        }).orElseGet(()->Header.ERROR("데이터 없음."));
    }

    private Header<ItemApiResponse> getResponse(Item item){
        ItemApiResponse body= ItemApiResponse.builder()
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
