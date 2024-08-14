package com.tvdgapp.services.qutoe;

import com.tvdgapp.dtos.qutoe.QuoteRequestDto;
import com.tvdgapp.models.quote.Quote;
import com.tvdgapp.services.generic.TvdgEntityService;

public interface QuoteService extends TvdgEntityService<Long, Quote> {

    public Quote createQuote(QuoteRequestDto quoteRequestDto);
}
