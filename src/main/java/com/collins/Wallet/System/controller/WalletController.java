package com.collins.Wallet.System.controller;

import com.collins.Wallet.System.dtos.ResponseDto;
import com.collins.Wallet.System.dtos.ResponseDtos.TransferRespDto;
import com.collins.Wallet.System.dtos.ResponseDtos.UserResponseDto;
import com.collins.Wallet.System.dtos.ResquestDto.CreateUserRequestDto;
import com.collins.Wallet.System.dtos.ResquestDto.DoTransDto;
import com.collins.Wallet.System.dtos.ResquestDto.FundAccountRequestDto;
import com.collins.Wallet.System.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create_user")
    public ResponseEntity<ResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto request) throws InterruptedException {
        Thread.sleep(2000);
        return walletService.createUser(request);
    }


    @PostMapping("/transfer_funds")
    public ResponseEntity<TransferRespDto> transferFunds(@Valid @RequestBody DoTransDto transferRequest,
                                        @RequestHeader("Idempotency-Key") String idempotencyKey) {
        return walletService.transfer(transferRequest, idempotencyKey);
    }

    @PostMapping("/fund_account")
    public ResponseEntity<TransferRespDto> fundAccount(@Valid @RequestBody FundAccountRequestDto fundAccountRequestDto) {
        return walletService.fundAccount(fundAccountRequestDto);
    }

    @GetMapping("/getList_Users")
    public ResponseEntity<List<UserResponseDto>> getAllUser() throws InterruptedException {
        Thread.sleep(2000);
        return walletService.getAllUser();
    }
}
