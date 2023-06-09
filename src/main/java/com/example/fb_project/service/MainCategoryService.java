package com.example.fb_project.service;

import com.example.fb_project.dto.MainCategoryDto;
import com.example.fb_project.dto.ProductDto;
import com.example.fb_project.dto.SubCategoryDto;
import com.example.fb_project.entity.MainCategory;
import com.example.fb_project.entity.Product;
import com.example.fb_project.entity.SubCategory;
import com.example.fb_project.mapper.MainCategoryMapper;
import com.example.fb_project.mapper.ProductMapper;
import com.example.fb_project.mapper.SubCategoryMapper;
import com.example.fb_project.repository.MainCategoryRepository;
import com.example.fb_project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MainCategoryService {
    private final MainCategoryRepository mainCategoryRepository;

    private final MainCategoryMapper mainCategoryMapper;

    private final SubCategoryMapper subCategoryMapper;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public boolean createMainCategory(String title) {
        if (mainCategoryRepository.findByTitle(title).isPresent()) {
            throw new IllegalStateException("Main category already exists");
        }
        mainCategoryRepository.save(new MainCategory(title));
        return true;
    }

    public List<MainCategoryDto> getAllMainCategories() {
        List<MainCategory> mainCategories = mainCategoryRepository.findAll();
        List<MainCategoryDto> mainCategoryDto = mainCategoryMapper.toDtoList(mainCategories);

        for (int i = 0; i < mainCategories.size(); i++) {
            List<SubCategoryDto> subCategoriesDto = subCategoryMapper.toListDto(mainCategories.get(i).getSubCategories());
            mainCategoryDto.get(i).setSubCategories(subCategoriesDto);
        }
        return mainCategoryDto;
    }

    public MainCategoryDto getMainCategoriesByIdWithProducts(String id) {
        MainCategory mainCategory = mainCategoryRepository.
                findById(new ObjectId(id)).orElseThrow(() ->
                        new IllegalStateException("The main category with id: " + id + " not found"));

        MainCategoryDto mainCategoryDto = mainCategoryMapper.toDto(mainCategory);

        List<SubCategory> subCategories = mainCategory.getSubCategories();
        for (int i = 0; i < subCategories.size(); i++) {

            List<Product> productsFromSubCategory = subCategories.get(i).getProducts();
            List<Product> productForMainCategory = new ArrayList<>(10);

            Random random = new Random();
            int j = 0;

            while (j < 10) {
                int randomNumber = random.nextInt(productsFromSubCategory.size());
                Product product = productsFromSubCategory.get(randomNumber);
                if (!productForMainCategory.contains(product)) {
                    productForMainCategory.add(product);
                    j++;
                }
            }

            List<ProductDto> productDtoList = productMapper.toDtoList(productForMainCategory);

            mainCategoryDto.getSubCategories().get(i).setProductDtoList(productDtoList);
        }


        Collections.shuffle(mainCategoryDto.getSubCategories());

        return mainCategoryDto;
    }

    public MainCategoryDto getMainCategoriesById(String id) {
        MainCategory mainCategory = mainCategoryRepository.
                findById(new ObjectId(id)).orElseThrow(() ->
                        new IllegalStateException("The main category with id: " + id + " not found"));

        MainCategoryDto mainCategoryDto = mainCategoryMapper.toDto(mainCategory);

        mainCategoryDto.setSubCategories(subCategoryMapper.toListDto(mainCategory.getSubCategories()));

        return mainCategoryDto;
    }

    public MainCategoryDto getMainCategoriesByTitle(String title) {
        MainCategory mainCategory = mainCategoryRepository.
                findByTitle(title).orElseThrow(() ->
                        new IllegalStateException("The main category with id: " + title + " not found"));

        MainCategoryDto mainCategoryDto = mainCategoryMapper.toDto(mainCategory);

        mainCategoryDto.setSubCategories(subCategoryMapper.toListDto(mainCategory.getSubCategories()));

        return mainCategoryDto;
    }

    public Document getAllMainCategoriesWithTenProduct() {
        List<MainCategory> mainCategories = mainCategoryRepository.findAll();

        Document document = new Document();
        Random random = new Random();
        SubCategory subCategory;
        Product product;

        for (MainCategory mainCategory : mainCategories) {
            List<ProductDto> productDtoList = new ArrayList<>(10);
            for (int i = 0; i < 10; i++) {
                getAndAddProductInProductDtoList(random, mainCategory, productDtoList);
            }
            document.put(mainCategory.getTitle(), productDtoList);
        }
        return document;
    }

    private void getAndAddProductInProductDtoList(Random random, MainCategory mainCategory, List<ProductDto> productDtoList) {
        SubCategory subCategory;
        Product product;
        int randomSubCategory = random.nextInt(0, mainCategory.getSubCategories().size());
        subCategory = mainCategory.getSubCategories().get(randomSubCategory);
        int randomProduct = random.nextInt(0, subCategory.getProducts().size());
        product = subCategory.getProducts().get(randomProduct);
        productDtoList.add(productMapper.toDto(product));
    }
}
