package com.example.study.model.entity;

import com.example.study.model.enumClass.OrderGroupOrderType;
import com.example.study.model.enumClass.OrderGroupPaymentType;
import com.example.study.model.enumClass.OrderGroupStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "orderDetailList"})
@EntityListeners(AuditingEntityListener.class)
@Builder
@Accessors(chain = true)
public class OrderGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderGroupStatus status; // REGISTERED / UNREGISTERED / WAITING

    @Enumerated(EnumType.STRING)
    private OrderGroupOrderType orderType; // 주문의 형태 - 일괄? 개별?

    private String revAddress;

    private String revName;

    @Enumerated(EnumType.STRING)
    private OrderGroupPaymentType paymentType; // 결제 방식 - 현금? 카드?

    private BigDecimal totalPrice;

    private Integer totalQuantity;

    private LocalDateTime orderAt;

    private LocalDateTime arrivalDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    //OrderGroup N : User 1
    @ManyToOne
    private User user;

    //OrderGroup 1 : OrderDetail N
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderGroup")
    private List<OrderDetail> orderDetailList;
}
