package com.example.bdcsamsungdevelopertest.common.util;

import com.example.bdcsamsungdevelopertest.common.exception.InternalServerException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.bdcsamsungdevelopertest.common.util.RequestValue.RequestType.*;
import static com.example.bdcsamsungdevelopertest.common.util.StringUtilExtension.subStringDomainFromUri;

@Component
public class RequestValue {

    public static Map<String, Map<String, Map<String, Map<String, Boolean>>>> URI_REQUEST_FORMAT = new HashMap<>();

    private final String PROJECT_DIRECTORY = new File("").getAbsolutePath() + "/src/main/java/com/example/bdcsamsungdevelopertest";
    private final String ROOT_PACKAGE_NAME = "com.example.bdcsamsungdevelopertest";

    @PostConstruct
    public void extractStaticValues() {
        File[] controllerFiles = FileUtil.retrieveFilesByDirectory(PROJECT_DIRECTORY + "/interfaces");
        List<String> controllerClassNames = FileUtil.retrieveClassNames(controllerFiles);

        // TODO deprecate
        File[] dtoFiles = FileUtil.retrieveFilesByDirectory(PROJECT_DIRECTORY + "/interfaces/dto");
        List<String> dtoClassNames = FileUtil.retrieveClassNames(dtoFiles);

        List<BeforeFilterClass> listOfControllerProperties = extractControllerProperties(controllerClassNames);
        Map<String, Map<String, Map<String, Map<String, Boolean>>>> groupedResult = groupControllerProperties(listOfControllerProperties);
        URI_REQUEST_FORMAT.putAll(groupedResult);
    }

    private List<BeforeFilterClass> extractControllerProperties(
        List<String> controllerClassNames
    ) {
        List<BeforeFilterClass> beforeFilterClassList = new ArrayList<>();
        for(String className: controllerClassNames) {
            Class<?> clazz = FileUtil.retrieveClassInRootDirectoryByClassName(ROOT_PACKAGE_NAME + ".interfaces." + className);
            Method[] controllerDeclaredFields = clazz.getDeclaredMethods();
            String baseUri = extractBaseUrl(clazz.getDeclaredAnnotations());
            for(Method controllerMethod : controllerDeclaredFields) {
                PostMapping postMap = controllerMethod.getAnnotation(PostMapping.class);
                GetMapping getMap = controllerMethod.getAnnotation(GetMapping.class);
                PutMapping putMap = controllerMethod.getAnnotation(PutMapping.class);
                DeleteMapping deleteMap = controllerMethod.getAnnotation(DeleteMapping.class);

                String constructedUri = "";
                String requestType = "";
                if(postMap != null) {
                    requestType = POST.name();
                    constructedUri = requestType + "]" + baseUri + postMappingUrlExtraction(postMap);
                } else if(getMap != null) {
                    requestType = GET.name();;
                    constructedUri = requestType + "]" + baseUri + getMappingUrlExtraction(getMap);
                } else if(putMap != null) {
                    requestType = PUT.name();;
                    constructedUri = requestType + "]" + baseUri + putMappingUrlExtraction(putMap);
                } else if(deleteMap != null) {
                    requestType = DELETE.name();;
                    constructedUri = requestType + "]" + baseUri + deleteMappingUrlExtraction(deleteMap);
                } else {
                    throw new InternalServerException("No annotations exists");
                }
                String domainName = subStringDomainFromUri(baseUri);
                Map<String, Boolean> extractedMethodParameters = methodParameterExtraction(controllerMethod);
                beforeFilterClassList.add(new BeforeFilterClass(requestType, domainName, constructedUri, extractedMethodParameters));
            }
        }
        return beforeFilterClassList;
    }

    private Map<String, Boolean> methodParameterExtraction(Method method) {
        Parameter[] parameters = method.getParameters();
        Map<String, Boolean> requireMapping = new HashMap<>();

        for(Parameter parameter : parameters) {
            List<Annotation> filteredAnnotations = filterControllerAnnotations(parameter.getAnnotations());
            for (Annotation annotation : filteredAnnotations) {
                if (annotation instanceof RequestBody) {
                    Class<?> parameterType = parameter.getType();
                    Field[] fields = parameterType.getDeclaredFields();
                    for(Field field : fields) {
                        requireMapping.put(field.getName(), true);
                    }
                } else if(annotation instanceof PathVariable pathVariable) {
                    String name = pathVariable.value();
                    Boolean require = pathVariable.required();
                    requireMapping.put(name, require);
                } else if(annotation instanceof RequestParam requestParam) {
                    String name = requestParam.value();
                    Boolean require = requestParam.required();
                    requireMapping.put(name, require);
                }
            }
        }
        return requireMapping;
    }

    private List<Annotation> filterControllerAnnotations(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .filter( nestedAnnotation ->
                        nestedAnnotation instanceof RequestBody ||
                        nestedAnnotation instanceof PathVariable ||
                        nestedAnnotation instanceof RequestParam
                ).toList();
    }

    private String extractBaseUrl(Annotation[] annotations){
        String baseUrl = "";
        for(Annotation annotation : annotations) {
            if(annotation instanceof RequestMapping requestMapping) {
                String[] requestMappingValues = requestMapping.value();
                if(requestMappingValues.length == 0) throw new InternalServerException("No requestMapping value detected");
                baseUrl = requestMappingValues[0];
            }
        }
        return baseUrl;
    }

    private String postMappingUrlExtraction(PostMapping postMapping) {
        String[] paths = postMapping.value();
        if(paths.length == 0) throw new InternalServerException("No url has been set");
        return paths[0];
    }

    private String getMappingUrlExtraction(GetMapping getMapping) {
        String[] paths = getMapping.value();
        if(paths.length == 0) throw new InternalServerException("No url has been set");
        return paths[0];
    }

    private String putMappingUrlExtraction(PutMapping putMapping) {
        String[] paths = putMapping.value();
        if(paths.length == 0) throw new InternalServerException("No url has been set");
        return paths[0];
    }

    private String deleteMappingUrlExtraction(DeleteMapping deleteMapping) {
        String[] paths = deleteMapping.value();
        if(paths.length == 0) throw new InternalServerException("No url has been set");
        return paths[0];
    }

    private Map<String, Map<String, Map<String, Map<String, Boolean>>>> groupControllerProperties(
        List<BeforeFilterClass> listOfControllerProperties
    ) {
        return listOfControllerProperties.stream()
                .collect(Collectors.groupingBy(
                        BeforeFilterClass::type,
                        Collectors.groupingBy(
                                BeforeFilterClass::domainType,
                                Collectors.groupingBy(
                                        BeforeFilterClass::url,
                                        Collectors.flatMapping(
                                                beforeFilter -> beforeFilter.parameterMap().entrySet().stream(),
                                                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                                        )
                                )
                        )
                ));
    }

    enum RequestType {
        POST,
        GET,
        PUT,
        DELETE
    }

    public record BeforeFilterClass(
        String type,
        String domainType,
        String url,
        Map<String, Boolean> parameterMap
    ) {}
}
