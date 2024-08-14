package com.tvdgapp.data;


import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.services.reference.init.InitializationDatabase;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("!test")
public class InitializationLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializationLoader.class);

    @Value("${db.init.data:true}")
    private boolean initDefaultData;


    @Autowired
    private InitializationDatabase initializationDatabase;

//    @Autowired
//    protected AdminUserDetailsService adminUserDetailsService;



    @PostConstruct
    //@Transactional
    public void init() {

        try {

            //Check flag to populate or not the database
            if (!this.initDefaultData) {
                //return;
            }

           // if (initializationDatabase.isEmpty()) {

                //All default data to be created
                LOGGER.info(String.format("%s : Tvdgapp database is empty, populate it....", "tvdgapp"));

                initializationDatabase.populate(SchemaConstant.DEFAULT_APP_NAME);

           // }

        } catch (Exception e) {
            LOGGER.error("Error in the init method", e);
        }

    }


}
