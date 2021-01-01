package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //JPA Spec 에서 사용
    protected Address() {
    }

    //값 타입은 변경 불가능하게 설계
    // @Setter 를 빼서 ,
    // 생성할때만 값 생성하고, 변경이 불가능함.
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
