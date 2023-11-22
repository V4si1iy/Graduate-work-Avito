package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.Ad;
import ru.skypro.homework.model.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.dto.ExtendedAd;
import ru.skypro.homework.model.entity.AdModel;

import java.util.Optional;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
@Component
public interface AdMapper {
    default Ad adModelToAdDto(AdModel adModel){
        Ad ad = new Ad();
        ad.setAuthor(adModel.getUser().getId());
        ad.setImage(Optional.ofNullable(adModel.getImage())
                .orElse(null));
        ad.setPk(adModel.getId());
        ad.setPrice(adModel.getPrice());
        ad.setTitle(adModel.getTitle());
        return ad;
    }
    default AdModel CreateAdsToAdsModel(CreateOrUpdateAd createOrUpdateAd) {
        AdModel adModel = new AdModel();
        adModel.setPrice(createOrUpdateAd.getPrice());
        adModel.setTitle(createOrUpdateAd.getTitle());
        adModel.setDescription(createOrUpdateAd.getDescription());
        return adModel;
    }
    default ExtendedAd adsModelToExtendedAdds(AdModel adModel) {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adModel.getId());
        extendedAd.setAuthorFirstName(adModel.getUser().getFirstName());
        extendedAd.setAuthorLastName(adModel.getUser().getLastName());
        extendedAd.setEmail(adModel.getUser().getUsername());
        extendedAd.setImage(Optional.ofNullable(adModel.getImage())
                .orElse(null));
        extendedAd.setPhone(adModel.getUser().getPhone());
        extendedAd.setPrice((adModel.getPrice()));
        extendedAd.setTitle(adModel.getTitle());
        extendedAd.setDescription((adModel.getDescription()));
        return extendedAd;
    }
    default AdModel CreateAdsToAdsModel(AdModel adModel, CreateOrUpdateAd createOrUpdateAd){
        adModel.setPrice(isNull(createOrUpdateAd.getPrice()) ? adModel.getPrice() : createOrUpdateAd.getPrice());
        adModel.setTitle(isNull(createOrUpdateAd.getTitle()) ? adModel.getTitle() : createOrUpdateAd.getTitle());
        return adModel;
    }
}
