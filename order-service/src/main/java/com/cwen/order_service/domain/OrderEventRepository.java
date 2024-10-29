package com.cwen.order_service.domain;

import org.springframework.data.jpa.repository.JpaRepository;

interface OrderEventRepository extends JpaRepository<OrderEventEntity, Long> {

}
