package com.tvdgapp.services.fileclaim;

import com.tvdgapp.dtos.fileclaim.FileClaimRequestDto;
import com.tvdgapp.dtos.fileclaim.FileClaimResponseDto;
import com.tvdgapp.models.fileclaims.FileClaims;
import com.tvdgapp.services.generic.TvdgEntityService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileClaimService extends TvdgEntityService<Long, FileClaims> {

    public FileClaimResponseDto createFileClaim(FileClaimRequestDto requestDto );
    Optional<String> uploadShipmentReceipt(long userId, MultipartFile uploadReceipt);
}
