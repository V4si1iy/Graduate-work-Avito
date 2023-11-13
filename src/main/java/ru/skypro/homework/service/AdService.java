package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.dto.Ad;
import ru.skypro.homework.model.dto.Ads;
import ru.skypro.homework.model.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.dto.ExtendedAd;
import ru.skypro.homework.model.entity.AdModel;
import ru.skypro.homework.repository.AdRepository;

import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AdService {
    private final AdRepository repository;
    private final AdMapper adMapper;

    public Ad create(CreateOrUpdateAd createAd,String author ) throws EntityExistsException {
        log.info("Executing the method to create a new Ad");

        // Преобразование DTO в сущность
        AdModel adModel = adMapper.CreateAdsToAdsModel(ad);

        // Проверка наличия объявления по уникальному идентификатору
        if (adModel.getId() != null && repository.existsById(adModel.getId())) {
            log.error("Failed to create Ad. Ad with id {} already exists.", adModel.getId());
            throw new EntityExistsException("Ad with id " + adModel.getId() + " already exists");
        }

        // Сохранение сущности
        AdModel savedAdModel = repository.save(adModel);

        // Преобразование сохраненной сущности обратно в DTO и возврат
        Ad savedAd = adMapper.adModelToAdDto(savedAdModel);

        log.info("Ad with id {} created successfully", savedAd.getPk());
        return savedAd;
    }

    public ExtendedAd getExtendedAdById(Long adId) throws EntityNotFoundException {
        log.info("Fetching Ad with id {}", adId);

        try {
            AdModel adModel = repository.findById(adId)
                    .orElseThrow(() -> new EntityNotFoundException("Ad with id " + adId + " not found"));

            log.info("Fetched Ad: {}", adModel);
            return adMapper.adsModelToExtendedAdds(adModel);
        } catch (Exception e) {
            log.error("Error fetching Ad with id {}", adId, e);
            throw e;
        }
    }

    private Ad getAdById(Long adId) throws EntityNotFoundException {
        log.info("Fetching Ad with id {}", adId);

        try {
            AdModel adModel = repository.findById(adId)
                    .orElseThrow(() -> new EntityNotFoundException("Ad with id " + adId + " not found"));

            log.info("Fetched Ad: {}", adModel);
            return adMapper.adModelToAdDto(adModel);
        } catch (Exception e) {
            log.error("Error fetching Ad with id {}", adId, e);
            throw e;
        }
    }

    public Ad updateAd(Long adId, CreateOrUpdateAd updatedAd) throws EntityNotFoundException {
        log.info("Updating Ad with id {}", adId);

        // Проверка существования объявления
        Ad existingAd = getAdById(adId);
        existingAd.setTitle(updatedAd.getTitle());
        existingAd.setPrice(updatedAd.getPrice());
        existingAd.setDescription(updatedAd.getDescription());
        AdModel adModel= adMapper.CreateAdsToAdsModel(existingAd);
        // Сохранение обновленной сущности
        AdModel updatedAdModel = repository.save(adModel);

        log.info("Ad with id {} updated successfully", adId);
        return adMapper.adModelToAdDto(updatedAdModel);
    }

    public void deleteAd(Long adId) throws EntityNotFoundException {
        log.info("Deleting Ad with id {}", adId);
        try {
            AdModel existingAdModel = repository.findById(adId)
                    .orElseThrow(() -> new EntityNotFoundException("Ad with id " + adId + " not found"));

            repository.delete(existingAdModel);

            log.info("Ad with id {} deleted successfully", adId);
        } catch (Exception e) {
            log.error("Error deleting Ad with id {}", adId, e);
            throw e;
        }
    }

    public Ads getAllAds() {
        log.info("Fetching all Ads");
        Ads allAd = new Ads();
        // Получение списка всех объявлений
        allAd.setCount(Integer.parseInt(String.valueOf(repository.count())));
        allAd.setResults(repository.findAll().stream().map(adMapper::adModelToAdDto).collect(Collectors.toList()));
        return allAd;
    }
}
