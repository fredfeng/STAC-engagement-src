/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost.coach;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class MultipartHelper {
    private static final int MAX_POST_LENGTH = 32768;

    public static String grabMultipartFieldContent(HttpExchange httpExchange, String fieldName) {
        if (httpExchange == null) {
            return MultipartHelper.grabMultipartFieldContentHelp();
        }
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("Field name may not be blank or null");
        }
        HttpExchangeRequestContext context = new HttpExchangeRequestContext(httpExchange);
        String result = null;
        try {
            FileUpload fileUpload = new FileUpload();
            FileItemIterator iterator = fileUpload.getItemIterator(context);
            while (iterator.hasNext()) {
                FileItemStream fileItemStream = iterator.next();
                String name = fileItemStream.getFieldName();
                if (!name.equals(fieldName)) continue;
                result = IOUtils.toString(fileItemStream.openStream(), "UTF-8");
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error parsing multipart message: " + e.getMessage(), e);
        }
        return result;
    }

    private static String grabMultipartFieldContentHelp() {
        throw new IllegalArgumentException("HttpExchange may not be null");
    }

    public static Map<String, String> getMultipartFieldContent(HttpExchange httpExchange) {
        if (httpExchange == null) {
            throw new IllegalArgumentException("HttpExchange may not be null");
        }
        HttpExchangeRequestContext context = new HttpExchangeRequestContext(httpExchange);
        Object result = null;
        HashMap<String, String> postFields = new HashMap<String, String>();
        try {
            FileUpload fileUpload = new FileUpload();
            FileItemIterator iterator = fileUpload.getItemIterator(context);
            while (iterator.hasNext()) {
                FileItemStream fileItemStream = iterator.next();
                String name = fileItemStream.getFieldName();
                String value = IOUtils.toString(fileItemStream.openStream(), "UTF-8");
                postFields.put(name, value);
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error parsing multipart message: " + e.getMessage(), e);
        }
        return postFields;
    }

    public static Map<String, List<String>> fetchMultipartFieldContentDuplicates(HttpExchange httpExchange) {
        if (httpExchange == null) {
            throw new IllegalArgumentException("HttpExchange may not be null");
        }
        HttpExchangeRequestContext context = new HttpExchangeRequestContext(httpExchange);
        try {
            FileUpload fileUpload = new FileUpload();
            DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
            fileUpload.setFileItemFactory(fileItemFactory);
            Map<String, List<FileItem>> fieldItems = fileUpload.parseParameterMap(context);
            return MultipartHelper.extractFieldsFromParameterMap(fieldItems, fieldItems.keySet());
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error parsing multipart message: " + e.getMessage(), e);
        }
    }

    public static Map<String, List<String>> getMultipartValues(HttpExchange httpExchange, Set<String> fieldNames) {
        if (httpExchange == null) {
            return MultipartHelper.fetchMultipartValuesAssist();
        }
        if (fieldNames == null) {
            throw new IllegalArgumentException("Field Names may not be null");
        }
        HttpExchangeRequestContext context = new HttpExchangeRequestContext(httpExchange);
        FileUpload fileUpload = new FileUpload();
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileUpload.setFileItemFactory(fileItemFactory);
        HashMap<String, List<String>> fieldNameValues = new HashMap<String, List<String>>();
        try {
            Map<String, List<FileItem>> parameterMap = fileUpload.parseParameterMap(context);
            for (String fieldName : fieldNames) {
                List<FileItem> items = parameterMap.get(fieldName);
                if (items == null || items.isEmpty()) continue;
                List<String> values = fieldNameValues.get(fieldName);
                if (values == null) {
                    values = new ArrayList<String>();
                    fieldNameValues.put(fieldName, values);
                }
                for (int a = 0; a < items.size(); ++a) {
                    MultipartHelper.fetchMultipartValuesManager(items, values, a);
                }
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error parsing multipart message: " + e.getMessage(), e);
        }
        return fieldNameValues;
    }

    private static void fetchMultipartValuesManager(List<FileItem> items, List<String> values, int c) {
        FileItem item = items.get(c);
        values.add(item.getString());
    }

    private static Map<String, List<String>> fetchMultipartValuesAssist() {
        throw new IllegalArgumentException("HttpExchange may not be null");
    }

    private static Map<String, List<String>> extractFieldsFromParameterMap(Map<String, List<FileItem>> parameterMap, Set<String> fields) {
        HashMap<String, List<String>> fieldNameValues = new HashMap<String, List<String>>();
        for (String fieldName : fields) {
            List<FileItem> items = parameterMap.get(fieldName);
            if (items == null || items.isEmpty()) continue;
            List<String> values = fieldNameValues.get(fieldName);
            if (values == null) {
                values = new ArrayList<String>();
                fieldNameValues.put(fieldName, values);
            }
            for (int k = 0; k < items.size(); ++k) {
                MultipartHelper.extractFieldsFromParameterMapAdviser(items, values, k);
            }
        }
        return fieldNameValues;
    }

    private static void extractFieldsFromParameterMapAdviser(List<FileItem> items, List<String> values, int a) {
        FileItem item = items.get(a);
        values.add(item.getString());
    }

    public static Map<String, String> takeMultipartFile(HttpExchange httpExchange, Set<String> allFieldNames, String fileFieldName, Path fileDestDir, String fileName) {
        if (httpExchange == null) {
            return MultipartHelper.takeMultipartFileService();
        }
        if (allFieldNames == null) {
            return MultipartHelper.obtainMultipartFileHelper();
        }
        if (StringUtils.isBlank(fileFieldName)) {
            return MultipartHelper.pullMultipartFileTarget();
        }
        if (fileDestDir == null) {
            throw new IllegalArgumentException("File Destination Directory may not be null");
        }
        HttpExchangeRequestContext context = new HttpExchangeRequestContext(httpExchange);
        FileUpload fileUpload = new FileUpload();
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileUpload.setFileItemFactory(fileItemFactory);
        HashMap<String, String> fieldNameItems = new HashMap<String, String>();
        InputStream fileIn = null;
        if (!allFieldNames.contains(fileFieldName)) {
            HashSet<String> newFieldNames = new HashSet<String>(allFieldNames);
            newFieldNames.add(fileFieldName);
            allFieldNames = newFieldNames;
        }
        try {
            Map<String, List<FileItem>> parameterMap = fileUpload.parseParameterMap(context);
            for (String fieldName : allFieldNames) {
                List<FileItem> items = parameterMap.get(fieldName);
                if (items == null) continue;
                if (items.size() == 1) {
                    String fileItem;
                    FileItem item = items.get(0);
                    if (fieldName.equals(fileFieldName)) {
                        fileIn = item.getInputStream();
                        fileItem = item.getName();
                    } else {
                        fileItem = item.getString();
                    }
                    fieldNameItems.put(fieldName, fileItem);
                    if (item.getContentType() != null)
                        fieldNameItems.put("MIME", item.getContentType());
                    continue;
                }
                return MultipartHelper.grabMultipartFileFunction();
            }
            if (fileIn == null || StringUtils.isBlank((CharSequence)fieldNameItems.get(fileFieldName))) {
                throw new IllegalArgumentException("Missing required POST file file associated with field name " + fileFieldName);
            }
            File newfile = fileDestDir.toFile();
            if (!newfile.exists()) {
                MultipartHelper.obtainMultipartFileGuide(newfile);
            }
            Path trail = fileName != null ? Paths.get(fileDestDir.toString(), fileName) : Paths.get(fileDestDir.toString(), (String)fieldNameItems.get(fileFieldName));
            Files.copy(fileIn, trail, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error parsing multipart message: " + e.getMessage(), e);
        }
        return fieldNameItems;
    }

    private static void obtainMultipartFileGuide(File newfile) {
        newfile.mkdirs();
    }

    private static Map<String, String> grabMultipartFileFunction() {
        throw new IllegalArgumentException("Cannot handle more than one File Item for each Field Name");
    }

    private static Map<String, String> pullMultipartFileTarget() {
        throw new IllegalArgumentException("File Field Name many not be empty or null");
    }

    private static Map<String, String> obtainMultipartFileHelper() {
        throw new IllegalArgumentException("Field Names may not be null");
    }

    private static Map<String, String> takeMultipartFileService() {
        throw new IllegalArgumentException("HttpExchange may not be null");
    }

    public static List<String> getMultipartFieldItems(HttpExchange httpExchange, String fieldName) {
        if (httpExchange == null) {
            return MultipartHelper.obtainMultipartFieldItemsManager();
        }
        if (StringUtils.isBlank(fieldName)) {
            throw new IllegalArgumentException("Field name may not be blank or null");
        }
        HttpExchangeRequestContext context = new HttpExchangeRequestContext(httpExchange);
        FileUpload fileUpload = new FileUpload();
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileUpload.setFileItemFactory(fileItemFactory);
        ArrayList<String> itemStrings = new ArrayList<String>();
        try {
            List<FileItem> items = fileUpload.parseParameterMap(context).get(fieldName);
            if (items != null && !items.isEmpty()) {
                for (int j = 0; j < items.size(); ++j) {
                    FileItem item = items.get(j);
                    itemStrings.add(item.getString());
                }
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error parsing multipart message: " + e.getMessage(), e);
        }
        return itemStrings;
    }

    private static List<String> obtainMultipartFieldItemsManager() {
        throw new IllegalArgumentException("HttpExchange may not be null");
    }

    private static class HttpExchangeRequestContext
    implements RequestContext {
        private static final String CONTENT_TYPE = "Content-Type";
        private static final String MULTIPART_FORM_DATA = "multipart/form-data";
        private static final String BOUNDARY_EQUALS = "boundary=";
        private static final String CONTENT_LENGTH = "Content-Length";
        private static final String CONTENT_ENCODING = "Content-Encoding";
        private final String contentType;
        private final String contentEncoding;
        private final int contentLength;
        private final InputStream inputStream;

        public HttpExchangeRequestContext(HttpExchange httpExchange) {
            if (!"POST".equals(httpExchange.getRequestMethod())) {
                this.HttpExchangeRequestContextHome();
            }
            this.contentType = httpExchange.getRequestHeaders().getFirst("Content-Type");
            if (this.contentType == null) {
                this.HttpExchangeRequestContextCoordinator();
            }
            if (!this.contentType.startsWith("multipart/form-data")) {
                throw new IllegalArgumentException("Content type must be multipart/form-data");
            }
            int index = this.contentType.indexOf("boundary=", "multipart/form-data".length());
            if (index == -1) {
                throw new IllegalArgumentException("Content type must contain a boundary mapping");
            }
            String contentLengthHeader = httpExchange.getRequestHeaders().getFirst("Content-Length");
            if (contentLengthHeader == null) {
                this.HttpExchangeRequestContextService();
            }
            try {
                this.contentLength = Integer.parseInt(contentLengthHeader);
                if (this.contentLength > 32768) {
                    throw new IllegalArgumentException("Content length is too long: " + this.contentLength + ". It must be smaller than " + 32768 + " characters");
                }
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("Content-Length must be a number: " + e.getMessage(), e);
            }
            this.contentEncoding = httpExchange.getRequestHeaders().getFirst("Content-Encoding");
            this.inputStream = httpExchange.getRequestBody();
        }

        private void HttpExchangeRequestContextService() {
            new HttpExchangeRequestContextManager().invoke();
        }

        private void HttpExchangeRequestContextCoordinator() {
            throw new IllegalArgumentException("The Content-Type request header must exist");
        }

        private void HttpExchangeRequestContextHome() {
            throw new IllegalArgumentException("Only POST method is permitted");
        }

        @Override
        public String getCharacterEncoding() {
            return this.contentEncoding;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        @Deprecated
        @Override
        public int getContentLength() {
            return this.contentLength;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return this.inputStream;
        }

        private class HttpExchangeRequestContextManager {
            private HttpExchangeRequestContextManager() {
            }

            public void invoke() {
                throw new IllegalArgumentException("The Content-Length request header must exist");
            }
        }

    }

}

