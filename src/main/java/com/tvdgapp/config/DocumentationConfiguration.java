//package com.tvdgapp.config;
//
//import com.google.common.base.Predicate;
//import com.tvdgapp.constants.SchemaConstant;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.bind.annotation.RequestMethod;
//import springfox.documentation.RequestHandler;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.builders.ResponseMessageBuilder;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//import static io.swagger.models.auth.In.HEADER;
//import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//
//@Configuration
//@EnableSwagger2
//public class DocumentationConfiguration {
//
//    public static final Contact DEFAULT_CONTACT = new Contact("Emmanuel Ahola", "", "aholemmy@gmail.com");
//
//    @Bean
//    public Docket api() {
//
//        Set<String> produces = new HashSet<>();
//        produces.add("application/json");
//
//        Set<String> consumes = new HashSet<>();
//        consumes.add("application/json");
//
//        ModelRef errorModel = new ModelRef("ApiDataResponse");
//        List<ResponseMessage> responseMessages = Arrays.asList(
//                new ResponseMessageBuilder().code(500).message("500 messages").build());
//
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis((RequestHandlerSelectors.basePackage("com.tvdgapp.controllers"))
//                .build()
//                .apiInfo(apiInfo())
//                .securitySchemes(Collections.singletonList(new ApiKey("JWT", AUTHORIZATION, HEADER.name())))
//               /* .securityContexts(singletonList(
//                        SecurityContext.builder()
//                                .securityReferences(
//                                        singletonList(SecurityReference.builder()
//                                                .reference("JWT")
//                                                .scopes(new AuthorizationScope[0])
//                                                .build()
//                                        )
//                                )
//                                .build())
//                )*/
//                .produces(produces).consumes(consumes)
//                .useDefaultResponseMessages(false)
//                .globalResponseMessage(RequestMethod.POST, responseMessages)
//                .globalResponseMessage(RequestMethod.PUT, responseMessages)
//                .globalResponseMessage(RequestMethod.GET, responseMessages)
//                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
//
//    }
//
//    @SuppressWarnings("rawtypes")
//    private ApiInfo apiInfo() {
//        return new ApiInfo(
//                SchemaConstant.DEFAULT_APP_NAME,
//                "API for " + SchemaConstant.DEFAULT_APP_NAME + ". Contains public end points.",
//                "1.0",
//                "Terms of service",
//                DEFAULT_CONTACT,
//                "License of API", "API license URL", Collections.emptyList());
//    }
//
//
//    private static ArrayList<? extends SecurityScheme> securitySchemes() {
//        return (ArrayList<? extends SecurityScheme>) Stream.of(new ApiKey("Bearer", "Authorization", "header"))
//                .collect(Collectors.toList());
//    }
//
//}
