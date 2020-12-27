package com.example.study.model.network.request;

import com.example.study.model.enumClass.OrderGroupOrderType;
import com.example.study.model.enumClass.OrderGroupPaymentType;
import com.example.study.model.enumClass.OrderGroupStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderGroupApiRequest {

    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderGroupStatus status;

    @Enumerated(EnumType.STRING)
    private OrderGroupOrderType orderType;

    private String revAddress;

    private String revName;

    @Enumerated(EnumType.STRING)
    private OrderGroupPaymentType paymentType;

    private BigDecimal totalPrice;

    private Integer totalQuantity;

    private LocalDateTime orderAt;

    private LocalDateTime arrivalDate;

    private Long userId;
}
