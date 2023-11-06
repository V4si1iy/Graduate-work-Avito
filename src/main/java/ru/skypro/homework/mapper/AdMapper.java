package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.model.dto.Ad;
import ru.skypro.homework.model.dto.AdModel;
import ru.skypro.homework.model.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.dto.ExtendedAd;

import java.util.Optional;

import static java.util.Objects.isNull;

@Mapper
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
        adModel.setDescription(createOrUpdateAd.getDescription());
        adModel.setPrice(createOrUpdateAd.getPrice());
        adModel.setTitle(createOrUpdateAd.getTitle());
        return adModel;
    }
    default ExtendedAd adsModelToExtendedAdds(AdModel adModel) {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adModel.getId());
        extendedAd.setAuthorFirstName(adModel.getUser().getFirstName());
        extendedAd.setAuthorLastName(adModel.getUser().getLastName());
        extendedAd.setDescription(adModel.getDescription());
        extendedAd.setEmail(adModel.getUser().getUsername());
        extendedAd.setImage(Optional.ofNullable(adModel.getImage())
                .orElse(null));
        extendedAd.setPhone(adModel.getUser().getPhone());
        extendedAd.setPrice((adModel.getPrice()));
        extendedAd.setTitle(adModel.getTitle());
        return extendedAd;
    }
    default AdModel CreateAdsToAdsModel(AdModel adModel, CreateOrUpdateAd createOrUpdateAd){
        adModel.setDescription(isNull(createOrUpdateAd.getDescription()) ? adModel.getDescription() : createOrUpdateAd.getDescription());
        adModel.setPrice(isNull(createOrUpdateAd.getPrice()) ? adModel.getPrice() : createOrUpdateAd.getPrice());
        adModel.setTitle(isNull(createOrUpdateAd.getTitle()) ? adModel.getTitle() : createOrUpdateAd.getTitle());
        return adModel;
    }
}
