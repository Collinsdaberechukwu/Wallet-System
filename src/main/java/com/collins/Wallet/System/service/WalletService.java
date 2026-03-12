package com.collins.Wallet.System.service;

import com.collins.Wallet.System.dtos.ResponseDto;
import com.collins.Wallet.System.dtos.ResponseDtos.TransferRespDto;
import com.collins.Wallet.System.dtos.ResquestDto.CreateUserRequestDto;
import com.collins.Wallet.System.dtos.ResquestDto.DoTransDto;
import com.collins.Wallet.System.dtos.ResquestDto.FundAccountRequestDto;
import com.collins.Wallet.System.model.WalletBalance;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface WalletService {
    @Transactional
    ResponseEntity<ResponseDto> createUser(CreateUserRequestDto createUserRequestDto);

    @Transactional
    ResponseEntity<TransferRespDto> transfer(DoTransDto transferRequest, String idempotencyKey);

    @Transactional
    ResponseEntity<TransferRespDto> fundAccount(@Valid FundAccountRequestDto fundAccountRequestDto);


}
