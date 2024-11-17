package ai.dify.javaclient;

import okhttp3.*;
import com.alibaba.fastjson2.JSONObject;

/**
 * This class represents a client for interacting with the Dify Completion API.
 * It provides methods for creating completion messages using the API.
 */
public class CompletionClient extends DifyClient {
    /**
     * Constructs a new CompletionClient with the provided API key.
     *
     * @param apiKey The API key to use for authentication.
     */
    public CompletionClient(String apiKey) {
        super(apiKey);
    }
    /**
     * Constructs a new CompletionClient with the provided API key and base URL.
     *
     * @param apiKey   The API key to use for authentication.
     * @param baseUrl  The base URL of the Dify API.
     */
    public CompletionClient(String apiKey, String baseUrl) {
        super(apiKey, baseUrl);
    }

    /**
     * Creates a new completion message.
     *
     * @param inputs The input text for which completion is requested.
     * @param query  The query associated with the completion request.
     * @param user   The user associated with the completion request.
     * @param stream Whether to use streaming response mode.
     * @return The HTTP response containing the result of the API request.
     * @throws DifyClientException If an error occurs while sending the request.
     */
    public Response createCompletionMessage(String inputs, String query, String user, boolean stream) throws DifyClientException {
        JSONObject json = new JSONObject();
        JSONObject inputsJson = new JSONObject();
        inputsJson.put("query", query);
        json.put("inputs", inputsJson);
        json.put("user", user);
        json.put("response_mode", stream ? "streaming" : "blocking");

        return sendRequest(CREATE_COMPLETION_MESSAGE, null, createJsonPayload(json));
    }
}
