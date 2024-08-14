package com.tvdgapp.controllers.quote;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.qutoe.QuoteRequestDto;
import com.tvdgapp.dtos.qutoe.QuoteResponseDto;
import com.tvdgapp.models.quote.Quote;
import com.tvdgapp.services.qutoe.QuoteService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('manageQuote')")
    public ResponseEntity<ApiDataResponse<QuoteResponseDto>> createQuote(@RequestBody QuoteRequestDto quoteRequestDto) {
        Quote createdQuote = quoteService.createQuote(quoteRequestDto);
        QuoteResponseDto responseDto = mapToQuoteResponseDto(createdQuote);
        return ApiResponseUtils.response(HttpStatus.CREATED, responseDto, "Resource created successfully");
    }

    private QuoteResponseDto mapToQuoteResponseDto(Quote quote) {
        QuoteResponseDto responseDto = new QuoteResponseDto();
        responseDto.setId(quote.getId());
        responseDto.setSubject(quote.getSubject());
        responseDto.setDescription(quote.getDescription());
        responseDto.setCompanyName(quote.getCompanyName());
        responseDto.setIndustry(quote.getIndustry());
        responseDto.setFirstName(quote.getFirstName());
        responseDto.setLastName(quote.getLastName());
        responseDto.setEmail(quote.getEmail());
        responseDto.setCountry(quote.getCountry());
        responseDto.setPhoneNo(quote.getPhoneNo());
        responseDto.setState(quote.getState());
        responseDto.setCity(quote.getCity());
        responseDto.setZipcode(quote.getZipcode());
        responseDto.setStreetAddress(quote.getStreetAddress());
//        responseDto.setServiceOption(quote.getServiceOption());
        return responseDto;
    }
}

