package com.example.study.service;

import com.example.study.model.entity.Category;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.CategoryRequest;
import com.example.study.model.network.response.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryApiLogicService extends BaseService<CategoryRequest, CategoryResponse, Category> {

    @Override
    public Header<CategoryResponse> create(Header<CategoryRequest> request) {
        CategoryRequest body = request.getData();
        Category category = Category.builder()
                .type(body.getType())
                .title(body.getTitle())
                .build();

        Category newCategory = baseRepository.save(category);
        return getResponse(newCategory);
    }

    @Override
    public Header<CategoryResponse> read(Long id) {
        Optional<Category> opt = baseRepository.findById(id);
        return opt
                .map(category -> getResponse(category))
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<CategoryResponse> update(Header<CategoryRequest> request) {
        CategoryRequest body = request.getData();
        Optional<Category> opt = baseRepository.findById(body.getId());
        return opt
                .map(category->{
                    category
                            .setType(body.getType())
                            .setTitle(body.getTitle());
                    Category newCategory = baseRepository.save(category);
                    return newCategory;
                })
                .map(category->getResponse(category))
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        Optional<Category> opt = baseRepository.findById(id);
        return opt
                .map(category->{
                    baseRepository.delete(category);
                    return Header.OK();
                })
                .orElseGet(()->Header.ERROR("데이터 없음"));
    }

    public Header<CategoryResponse> getResponse(Category category){
        CategoryResponse res = CategoryResponse.builder()
                .id(category.getId())
                .type(category.getType())
                .title(category.getTitle())
                .build();

        return Header.OK(res);
    }
}
