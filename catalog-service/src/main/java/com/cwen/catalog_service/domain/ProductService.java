package com.cwen.catalog_service.domain;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {
    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public PagedResult<Product> getProducts(int pageNum){
        pageNum = pageNum <= 1 ? 0 : pageNum - 1;

        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(pageNum, 10);
        Page<Product> productPage = productRepo.findAll(pageable)
                .map(ProductMapper::toProduct);

        return new PagedResult<>(
                productPage.getContent(),
                productPage.getTotalElements(),
                productPage.getNumber() + 1,
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast(),
                productPage.hasNext(),
                productPage.hasPrevious()
        );

    }


}
