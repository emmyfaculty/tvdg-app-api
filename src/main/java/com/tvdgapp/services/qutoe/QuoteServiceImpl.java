package com.tvdgapp.services.qutoe;

import com.tvdgapp.dtos.qutoe.QuoteRequestDto;
import com.tvdgapp.models.quote.Quote;
import com.tvdgapp.repositories.quote.QuoteRepository;

import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuoteServiceImpl extends TvdgEntityServiceImpl<Long, Quote> implements QuoteService {

    private final QuoteRepository quoteRepository;


    @Override
    public Quote createQuote(QuoteRequestDto quoteRequestDto) {
        Quote quote = mapToQuoteEntity(quoteRequestDto);
        return quoteRepository.save(quote);
    }

    private Quote mapToQuoteEntity(QuoteRequestDto quoteRequestDto) {
        Quote quote = new Quote();
        quote.setSubject(quoteRequestDto.getSubject());
        quote.setDescription(quoteRequestDto.getDescription());
        quote.setCompanyName(quoteRequestDto.getCompanyName());
        quote.setIndustry(quoteRequestDto.getIndustry());
        quote.setFirstName(quoteRequestDto.getFirstName());
        quote.setLastName(quoteRequestDto.getLastName());
        quote.setEmail(quoteRequestDto.getEmail());
        quote.setPhoneNo(quoteRequestDto.getPhoneNo());
        quote.setCountry(quoteRequestDto.getCountry());
        quote.setState(quoteRequestDto.getState());
        quote.setCity(quoteRequestDto.getCity());
        quote.setZipcode(quoteRequestDto.getZipcode());
        quote.setStreetAddress(quoteRequestDto.getStreetAddress());
//        Long serviceOptionId = quoteRequestDto.getServiceOption().getId();
//        ServiceOption serviceOption = serviceOptionRepository.findById(serviceOptionId)
//                .orElseThrow(() -> new RuntimeException("Service option not found"));
//        quote.setServiceOption(serviceOption);
        return quote;
    }


}
