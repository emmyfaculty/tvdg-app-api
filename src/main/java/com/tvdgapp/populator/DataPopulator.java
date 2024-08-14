/**
 * 
 */
package com.tvdgapp.populator;


import com.tvdgapp.exceptions.ConversionRuntimeException;

public interface DataPopulator<Source,Target> {

    Target populate(Source source,Target target) throws ConversionRuntimeException;
    Target populate(Source source) throws ConversionRuntimeException;

}
