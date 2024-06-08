package com.DmitriyDevv.dto;

public record ResponseData<T>(T payload, int responseCode) {
}
