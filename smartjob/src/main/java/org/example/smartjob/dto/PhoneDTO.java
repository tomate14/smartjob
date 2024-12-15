package org.example.smartjob.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.smartjob.entities.Phone;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneDTO extends Phone {

    public PhoneDTO(String number, String cityCode, String countryCode) {
        this.number = number;
        this.cityCode = cityCode;
        this.countryCode = countryCode;
    }

    public static PhoneDTO buildFromPhone(Phone phone) {
        return new PhoneDTO(phone.getNumber(), phone.getCityCode(), phone.getCountryCode());
    }
}
