package com.tvdgapp.services.system;

import com.tvdgapp.dtos.system.SystemConfigurationDto;
import com.tvdgapp.models.SystemConfiguration;
import com.tvdgapp.services.generic.TvdgEntityService;

import java.util.Collection;
import java.util.Map;

public interface SystemConfigurationService  extends TvdgEntityService<Long, SystemConfiguration> {

    String findConfigValueByKey(String key);

    void createDefaultConfigurations();

    Collection<SystemConfigurationDto> fetchSystemConfigurations(String group);

    void updateSystemConfigurations(String group, Map<String,String> dto);
}
