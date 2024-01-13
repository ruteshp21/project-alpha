#!/usr/bin/env bash

spring init \
--boot-version=3.2.1 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=product-service \
--package-name=rp.rutepati.microservices.core.product \
--groupId=rp.rutepati.microservices.core.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-service

spring init \
--boot-version=3.2.1 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=review-service \
--package-name=rp.rutepati.microservices.core.review \
--groupId=rp.rutepati.microservices.core.review \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
review-service

spring init \
--boot-version=3.2.1 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=recommendation-service \
--package-name=rp.rutepati.microservices.core.recommendation \
--groupId=rp.rutepati.microservices.core.recommendation \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
recommendation-service

spring init \
--boot-version=3.2.1 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=product-composite-service \
--package-name=rp.rutepati.microservices.composite.product \
--groupId=rp.rutepati.microservices.composite.product \
--dependencies=actuator,webflux \
--version=1.0.0-SNAPSHOT \
product-composite-service
