package org.projects.productservice.api.service;

import lombok.RequiredArgsConstructor;
import org.projects.productservice.api.dto.BrandDto;
import org.projects.productservice.api.dto.BrandResponseDto;
import org.projects.productservice.api.exception.NotFoundException;
import org.projects.productservice.store.entity.Brand;
import org.projects.productservice.store.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository repository;
    private final BrandMapper mapper;

    @Transactional
    public Long createBrand(BrandDto dto) {
        var brand = mapper.toBrand(dto);
        var savedBrand = repository.save(brand);
        return savedBrand.getId();
    }

    public List<BrandResponseDto> getAllBrands() {
        return repository.findAll()
                .stream()
                .map(mapper::toBrandResponseDto)
                .collect(Collectors.toList());
    }

    public BrandResponseDto getBrand(Long id) {
        return repository.findById(id)
                .map(mapper::toBrandResponseDto)
                .orElseThrow(()-> new NotFoundException("Brand not found"));
    }

    @Transactional
    public  void updateBrand(Long id, BrandDto dto) {
       Brand existringBrand = repository.findById(id)
           .orElseThrow(() -> new NotFoundException("Brand not fount with id: "+id));
       existringBrand.setName(dto.name());
       existringBrand.setCountry(dto.country());
       existringBrand.setDescription(dto.description());
       repository.save(existringBrand);

   }


   @Transactional
    public void deleteBrandById(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Brand not found with ID: "+id);
        }
        repository.deleteById(id);
    }
}
