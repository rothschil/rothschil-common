package io.github.rothschil.common.handler;


import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.ExecChain;
import org.apache.hc.client5.http.classic.ExecChainHandler;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.BufferedHttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/
@Slf4j
public class LogExecChainHandler implements ExecChainHandler {

    private static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";
    private static final String APPLICATION_JSON = "application/json";
    private static final String TEXT_JSON = "text/json";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    @Override
    public ClassicHttpResponse execute(ClassicHttpRequest request, ExecChain.Scope scope, ExecChain chain)
            throws IOException, HttpException {
        long startTime = System.currentTimeMillis();

        logRequest(request);

        try {
            ClassicHttpResponse response = chain.proceed(request, scope);
            long duration = System.currentTimeMillis() - startTime;
            logResponse(response, duration);
            return response;
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            logError(request, duration, e);
            throw e;
        }
    }

    /**
     * 记录HTTP请求日志
     *
     * @param request HTTP请求
     */
    private void logRequest(ClassicHttpRequest request) {
        try {
            String method = request.getMethod();
            String uri = request.getUri().toString();
            String headers = formatHeaders(request.getHeaders());
            String entityInfo = extractRequestEntityInfo(request);

            log.info("\n┌───── HTTP REQUEST ─────────────────────────────\n" +
                            "│ Method: {}\n" +
                            "│ URI: {}\n" +
                            "│ Headers: {}\n" +
                            "│ Body: {}\n" +
                            "└────────────────────────────────────────────────",
                    method, uri, headers, entityInfo);
        } catch (Exception e) {
            log.warn("记录请求日志时出错: {}", e.getMessage());
        }
    }

    /**
     * 提取请求实体信息
     *
     * @param request HTTP请求
     * @return 请求体信息
     */
    private String extractRequestEntityInfo(ClassicHttpRequest request) {
        try {
            String contentType = getContentType(request.getHeaders());
            if (isMultipartFormData(contentType)) {
                return "[文件流请求体]";
            }
            return readJsonRequestBody(request);
        } catch (Exception e) {
            return "[读取请求体失败: " + e.getMessage() + "]";
        }
    }

    /**
     * 读取JSON请求体
     *
     * @param request HTTP请求
     * @return JSON请求体内容
     */
    private String readJsonRequestBody(ClassicHttpRequest request) {
        try {
            HttpEntity entity = request.getEntity();
            if (entity == null) {
                return "[无请求体]";
            }

            byte[] content = entityToBytes(entity);
            if (content.length == 0) {
                return "[空JSON请求体]";
            }
            return new String(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.debug("读取JSON请求体失败: {}", e.getMessage());
            return "[读取JSON请求体失败]";
        }
    }

    /**
     * 记录HTTP响应日志
     *
     * @param response HTTP响应
     * @param duration 请求耗时
     */
    private void logResponse(ClassicHttpResponse response, long duration) {
        try {
            String statusLine = response.getCode() + " " + response.getReasonPhrase();
            String headers = formatHeaders(response.getHeaders());
            String entityInfo = extractResponseEntityInfo(response);

            log.info("\n┌───── HTTP RESPONSE ────────────────────────────\n" +
                            "│ Status: {}\n" +
                            "│ Headers: {}\n" +
                            "│ Body: {}\n" +
                            "│ Duration: {}ms\n" +
                            "└────────────────────────────────────────────────",
                    statusLine, headers, entityInfo, duration);
        } catch (Exception e) {
            log.warn("记录响应日志时出错: {}", e.getMessage());
        }
    }

    /**
     * 提取响应实体信息
     *
     * @param response HTTP响应
     * @return 响应体信息
     */
    private String extractResponseEntityInfo(ClassicHttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return "[无响应体]";
            }

            if (isJsonResponse(entity, response.getHeaders())) {
                BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
                response.setEntity(bufferedHttpEntity);
                return readJsonResponseBody(bufferedHttpEntity);
            } else {
                return "[非JSON响应体]";
            }
        } catch (Exception e) {
            return "[读取响应体失败: " + e.getMessage() + "]";
        }
    }

    /**
     * 读取JSON响应体
     *
     * @param entity HTTP实体
     * @return JSON响应体内容
     */
    private String readJsonResponseBody(HttpEntity entity) {
        try {
            byte[] content = entityToBytes(entity);
            if (content.length == 0) {
                return "[空JSON响应体]";
            }
            return new String(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.debug("读取JSON响应体失败: {}", e.getMessage());
            return "[读取JSON响应体失败]";
        }
    }

    /**
     * 记录错误日志
     *
     * @param request  HTTP请求
     * @param duration 请求耗时
     * @param error    异常信息
     */
    private void logError(ClassicHttpRequest request, long duration, Exception error) {
        try {
            String method = request.getMethod();
            String uri = request.getUri().toString();
            log.error("\n┌───── HTTP ERROR ───────────────────────────────\n" +
                            "│ Method: {}\n" +
                            "│ URI: {}\n" +
                            "│ Duration: {}ms\n" +
                            "│ Error: {}\n" +
                            "└────────────────────────────────────────────────",
                    method, uri, duration, error.getMessage());
        } catch (Exception e) {
            log.warn("记录错误日志时出错: {}", e.getMessage());
        }
    }

    /**
     * 获取Content-Type头部信息
     *
     * @param headers 头部数组
     * @return Content-Type值
     */
    private String getContentType(Header[] headers) {
        return Arrays.stream(headers)
                .filter(h -> CONTENT_TYPE_HEADER.equalsIgnoreCase(h.getName()))
                .map(Header::getValue)
                .findFirst()
                .orElse(null);
    }

    /**
     * 格式化头部信息
     *
     * @param headers 头部数组
     * @return 格式化后的头部信息
     */
    private String formatHeaders(Header[] headers) {
        if (headers == null || headers.length == 0) {
            return "";
        }
        return Arrays.stream(headers)
                .map(header -> header.getName() + ": " + header.getValue())
                .collect(Collectors.joining("; "));
    }

    /**
     * 判断是否为multipart/form-data类型
     *
     * @param contentType Content-Type值
     * @return 是否为multipart/form-data类型
     */
    private boolean isMultipartFormData(String contentType) {
        return contentType != null && contentType.contains(MULTIPART_CONTENT_TYPE);
    }

    /**
     * 判断是否为JSON响应
     *
     * @param entity  HTTP实体
     * @param headers 头部信息
     * @return 是否为JSON响应
     */
    private boolean isJsonResponse(HttpEntity entity, Header[] headers) {
        String contentType = Optional.ofNullable(entity.getContentType())
                .orElse(getContentType(headers));

        return contentType != null &&
                (contentType.contains(APPLICATION_JSON) || contentType.contains(TEXT_JSON));
    }

    /**
     * 将HTTP实体转换为字节数组
     *
     * @param entity HTTP实体
     * @return 字节数组
     * @throws IOException IO异常
     */
    private byte[] entityToBytes(HttpEntity entity) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        entity.writeTo(baos);
        return baos.toByteArray();
    }
}

