package com.collins.Wallet.System.mapper;

import com.collins.Wallet.System.dtos.ResponseDtos.TransferRespDto;


import java.util.UUID;

public class WalletMapper {

    private WalletMapper(){}

    public static TransferRespDto toTransferResponse(String message) {

        TransferRespDto dto = new TransferRespDto();
        dto.setReference(generateReference());
        dto.setMessage(message);

        return dto;
    }

    private static String generateReference() {
        return UUID.randomUUID().toString();
    }
}
