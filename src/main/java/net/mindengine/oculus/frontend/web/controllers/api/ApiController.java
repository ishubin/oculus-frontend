package net.mindengine.oculus.frontend.web.controllers.api;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mindengine.oculus.experior.ClassUtils;
import net.mindengine.oculus.frontend.api.ApiError;
import net.mindengine.oculus.frontend.api.DELETE;
import net.mindengine.oculus.frontend.api.GET;
import net.mindengine.oculus.frontend.api.POST;
import net.mindengine.oculus.frontend.api.PUT;
import net.mindengine.oculus.frontend.api.Path;
import net.mindengine.oculus.frontend.api.RequestBody;
import net.mindengine.oculus.frontend.api.RequestVar;
import net.mindengine.oculus.frontend.config.Config;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class ApiController implements Controller {
    
    private Config config;
    private List<ApiMethod> apiMethods = new LinkedList<ApiMethod>();
    
    public ApiController() {
        Collection<Method> allMethods = ClassUtils.getAllMethods(getClass());
        for (Method method : allMethods) {
            
            Path path = method.getAnnotation(Path.class);
            if ( path != null ) {
                ApiMethod apiMethod = new ApiMethod();
                apiMethod.setMethod(method);
                apiMethod.setPattern(Pattern.compile(".*" + path.value()));
                
                apiMethod.setType(findRequestMethod(method));
                apiMethods.add(apiMethod);
            }
        }
    }

    private String findRequestMethod(Method method) {
        if ( method.getAnnotation(GET.class) != null) {
            return "GET";
        }
        else if ( method.getAnnotation(POST.class) != null) {
            return "POST";
        }
        else if ( method.getAnnotation(DELETE.class) != null) {
            return "DELETE";
        }
        else if ( method.getAnnotation(PUT.class) != null) {
            return "PUT";
        }
        return null;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        
        response.addHeader("Content-Type", "application/json");
        
        if (userIsAuthorized(request)) {
            MatchedMethod matchedMethod = findMatchedMethod(request);
            Object[] arguments = createMethodArguments(matchedMethod, request, response);
            
            try {
                Object result = matchedMethod.getMethod().invoke(this, arguments);
                if ( matchedMethod.getMethod().getReturnType().equals(Void.TYPE) ) {
                    return null;
                }
                else model.put("response", result);
            }
            catch (InvocationTargetException e) {
                response.setStatus(400);
                model.put("response", new ApiError(e.getTargetException().getClass().getName() + ": " + e.getTargetException().getMessage()));
                e.getTargetException().printStackTrace();
            }
        }
        else {
            response.setStatus(401);
            model.put("response", new ApiError("You are not authorized for this request."));
        }
        
        return new ModelAndView("jsonView", model);
    }
    
    private boolean userIsAuthorized(HttpServletRequest request) {
        if ( config == null ) {
            throw new IllegalStateException("Config is not set");
        }
        /**
         * In case if api.token is not specified authorization should not be checked.
         */
        if ( config.getApiSuperToken() == null || config.getApiSuperToken().trim().isEmpty() ) {
            return true;
        }
        
        String token = getTokenFromRequest(request);
        if ( token != null ) {
            return token.equals(config.getApiSuperToken());
        }
        return false;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authToken = request.getParameter("auth_token");
        if ( authToken != null ) {
            return authToken;
        }
        return request.getHeader("Auth-Token");
    }

    private MatchedMethod findMatchedMethod(HttpServletRequest request) {
        for ( ApiMethod apiMethod : apiMethods ) {
            
            String requiredMethodType = null;
            
            if ( apiMethod.getType() != null ) {
                requiredMethodType = apiMethod.getType();
            }
            
            if ( requiredMethodType == null || requiredMethodType.equals(request.getMethod())) {
                Matcher matcher = apiMethod.getPattern().matcher(request.getRequestURI());
                if ( matcher.matches() ) {
                    MatchedMethod matchedMethod = new MatchedMethod();
                    matchedMethod.setMethod(apiMethod.getMethod());
                    matchedMethod.setRequestMatcher(matcher);
                    return matchedMethod;
                }
            }
        }
        throw new IllegalArgumentException("No REST methods found");
    }
    
    private Object[] createMethodArguments(MatchedMethod matchedMethod, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Class<?>[] parameters = matchedMethod.getMethod().getParameterTypes();
        Annotation[][] parameterAnnotations = matchedMethod.getMethod().getParameterAnnotations();
        Object[] args = new Object[parameters.length];
        
        for ( int i = 0; i<parameters.length; i++ ) {
            if ( parameters[i].equals(HttpServletRequest.class) ) {
                args[i] = request;
            }
            else if ( parameters[i].equals(HttpServletResponse.class) ) {
                args[i] = response;
                
            }
            else {
                RequestVar var = findAnnotation(parameterAnnotations[i], RequestVar.class);
                if ( var != null ) {
                    args[i] = ClassUtils.createParameter(parameters[i], matchedMethod.getRequestMatcher().group(var.value()));
                }
                else if (findAnnotation(parameterAnnotations[i], RequestBody.class) != null ) {
                    String jsonString = IOUtils.toString(request.getInputStream());
                    
                    ObjectMapper objectMapper = new ObjectMapper();
                    args[i] = objectMapper.readValue(jsonString, parameters[i]);
                }
                else {
                    throw new IllegalArgumentException("Don't know how to map request");
                }
            }
        }
        
        return args;
    }

    @SuppressWarnings("unchecked")
    private <T> T  findAnnotation(Annotation[] annotations, Class<T> annotationClass) {
        if ( annotations != null ) {
            for ( Annotation annotation : annotations ) {
                if ( annotation.annotationType().equals(annotationClass)) {
                    return (T) annotation;
                }
            }
        }
        return null;
    }

    
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }


    private class ApiMethod {
        private Method method;
        private Pattern pattern;
        private String type;
        public Method getMethod() {
            return method;
        }
        public void setMethod(Method method) {
            this.method = method;
        }
        public Pattern getPattern() {
            return pattern;
        }
        public void setPattern(Pattern pattern) {
            this.pattern = pattern;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
    }
    
    
    private class MatchedMethod {
        private Method method;
        private Matcher requestMatcher;
        public Method getMethod() {
            return method;
        }
        public void setMethod(Method method) {
            this.method = method;
        }
        public Matcher getRequestMatcher() {
            return requestMatcher;
        }
        public void setRequestMatcher(Matcher requestMatcher) {
            this.requestMatcher = requestMatcher;
        }
    }
}
