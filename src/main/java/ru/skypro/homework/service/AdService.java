package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.dto.Ad;
import ru.skypro.homework.model.dto.Ads;
import ru.skypro.homework.model.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.dto.ExtendedAd;
import ru.skypro.homework.model.entity.AdModel;
import ru.skypro.homework.model.entity.Image;
import ru.skypro.homework.model.entity.UserModel;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AdService {
    private final AdRepository repository;
    private final ImageRepository imageRepository;
    private final UserService userService;
    private final AdMapper adMapper;

    public Ad create(CreateOrUpdateAd createAd, MultipartFile adFile, String author) throws EntityExistsException, IOException, EntityNotFoundException {
        log.info("Executing the method to create a new Ad");

        // Преобразование DTO в сущность
        AdModel adModel = adMapper.CreateAdsToAdsModel(createAd);

        // Проверка наличия объявления по уникальному идентификатору
//        if (adModel.getDescription() && repository.existsById(adModel.getId())) {
//            log.error("Failed to create Ad. Ad with id {} already exists.", adModel.getId());
//            throw new EntityExistsException("Ad with id " + adModel.getId() + " already exists");
//        }


        // Сохранения фотографии в бд
        Image image = new Image();
        image.setId(UUID.randomUUID().toString());
        image.setFileSize(adFile.getSize());
        image.setData(adFile.getBytes());
        image.setMediaType(adFile.getContentType());
        adModel.setImage("http://localhost:8080/image/{" + image.getId() + "}");
        image.setLink("http://localhost:8080/image/{" + image.getId() + "}");
        imageRepository.save(image);

        // Сохранение сущности
        UserModel userModel = userService.getUserByUsername(author);
        adModel.setUser(userModel);
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

    protected AdModel getAdById(Long adId) throws EntityNotFoundException {
        log.info("Fetching Ad with id {}", adId);

        try {
            AdModel adModel = repository.findById(adId)
                    .orElseThrow(() -> new EntityNotFoundException("Ad with id " + adId + " not found"));

            log.info("Fetched Ad: {}", adModel);
            return adModel;
        } catch (Exception e) {
            log.error("Error fetching Ad with id {}", adId, e);
            throw e;
        }
    }

    public CreateOrUpdateAd updateAd(Long adId, CreateOrUpdateAd updatedAd) throws EntityNotFoundException {
        log.info("Updating Ad with id {}", adId);

        // Проверка существования объявления
        AdModel existingAd = getAdById(adId);

        // Преобразование DTO в сущность
        AdModel adModel = adMapper.CreateAdsToAdsModel(updatedAd);
        adModel.setId(existingAd.getId());
        adModel.setUser(existingAd.getUser());
        adModel.setImage(existingAd.getImage());

        // Сохранение обновленной сущности
        AdModel updatedAdModel = repository.save(adModel);

        log.info("Ad with id {} updated successfully", adId);
        return updatedAd;
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

    public Ads getAdsUser(String author) throws EntityNotFoundException {
        log.info("Fetching user Ads");
        Ads allAd = new Ads();
        // Получение списка всех объявлений
        UserModel userModel = userService.getUserByUsername(author);
        allAd.setCount(repository.countByUser(userModel));
        allAd.setResults(repository.getAdModelByUser(userModel).stream().map(adMapper::adModelToAdDto).collect(Collectors.toList()));
        return allAd;
    }

    public void updateImageAd(MultipartFile file , Long id) throws EntityNotFoundException , IOException {
        // Проверка существования обьявления
        AdModel adModel = getAdById(id);
        // Сохранения фотографии в бд
        Image image = imageRepository.getImageByLink(adModel.getImage());
        image.setFileSize(file.getSize());
        image.setData(file.getBytes());
        image.setMediaType(file.getContentType());
        imageRepository.save(image);


    }

}
