package com.luban.xbongbong.api.helper.enums.customer;

import com.luban.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

 /**
  * @author hp
  */
 @Getter
 @AllArgsConstructor
 public enum XbbCustomerStatus implements BaseEnum<XbbCustomerStatus, String> {
     /***/
     潜在客户("1", "潜在客户"),
     初步接触("2", "初步接触"),
     持续跟进("3", "持续跟进"),
     成交客户("4", "成交客户"),
     忠诚客户("5", "忠诚客户"),
     无效客户("6", "无效客户"),
     ;
     private final String code;
     private final String name;

     public static Optional<XbbCustomerStatus> of(String code) {
         return Optional.ofNullable(BaseEnum.parseByCode(XbbCustomerStatus.class, code));
     }

     public static Optional<XbbCustomerStatus> ofName(String name) {
         return Arrays.stream(values())
                 .filter(i -> Objects.equals(name, i.getName()))
                 .findFirst();
     }
 }
