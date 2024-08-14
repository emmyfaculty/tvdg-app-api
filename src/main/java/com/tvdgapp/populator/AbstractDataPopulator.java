/**
 * 
 */
package com.tvdgapp.populator;





import jakarta.annotation.Nullable;

import java.util.Locale;

import static com.tvdgapp.utils.CommonUtils.castToNonNull;


/**
 * @author Oloba
 *
 */
@SuppressWarnings("NullAway.Init")
public abstract class AbstractDataPopulator<Source,Target> implements DataPopulator<Source, Target>
{

    private Locale locale;

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public Locale getLocale() {
		return locale;
	}
	


	@Override
	public Target populate(Source source)  {
	   return populate(source,castToNonNull(createTarget()));
	}


	protected abstract @Nullable Target createTarget();

   

}
