package com.tvdgapp.repositories.quote;

import com.tvdgapp.models.quote.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}

