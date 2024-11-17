package ai.dify.javaclient;

import ai.dify.javaclient.constants.DifyServerConstants;
import ai.dify.javaclient.http.DifyRoute;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * This class serves as a client for interacting with the Dify API.
 * It provides methods for sending various types of requests to the API.
 */
public class DifyClient {

    // Constants representing different API routes
    public static final DifyRoute APPLICATION = new DifyRoute("GET", "/parameters?user=%s");
    public static final DifyRoute FEEDBACK = new DifyRoute("POST", "/messages/%s/feedbacks");
    public static final DifyRoute CREATE_COMPLETION_MESSAGE = new DifyRoute("POST", "/completion-messages");
    public static final DifyRoute CREATE_CHAT_MESSAGE = new DifyRoute("POST", "/chat-messages");
    public static final DifyRoute GET_CONVERSATION_MESSAGES = new DifyRoute("GET", "/messages?%s");
    public static final DifyRoute GET_CONVERSATIONS = new DifyRoute("GET", "/conversations");
    public static final DifyRoute RENAME_CONVERSATION = new DifyRoute("PATCH", "/conversations/%s");
    public static final DifyRoute DELETE_CONVERSATION = new DifyRoute("DELETE", "/conversations/%s");

    private String apiKey;
    private final String baseUrl;
    private final OkHttpClient client;

    /**
     * Constructs a new DifyClient with the provided API key and default base URL.
     *
     * @param apiKey The API key to use for authentication.
     */
    public DifyClient(String apiKey) {
        this(apiKey, DifyServerConstants.BASE_URL);
    }

    /**
     * Constructs a new DifyClient with the provided API key and base URL.
     *
     * @param apiKey  The API key to use for authentication.
     * @param baseUrl The base URL of the Dify API.
     */
    public DifyClient(String apiKey, String baseUrl) {
        this(apiKey, baseUrl, 1L, TimeUnit.HOURS);
    }

    /**
     * Constructs a new DifyClient with the provided API key and base URL.
     *
     * @param apiKey  The API key to use for authentication.
     * @param baseUrl The base URL of the Dify API.
     * @param timeout The timeout number
     * @param timeOutUnit The timeout unit
     */
    public DifyClient(String apiKey, String baseUrl, Long timeout, TimeUnit timeOutUnit) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        this.client = builder.connectTimeout(timeout, timeOutUnit)
                .readTimeout(timeout, timeOutUnit)
                .writeTimeout(timeout, timeOutUnit)
                .build();
    }

    /**
     * Updates the API key used for authentication.
     *
     * @param apiKey The new API key.
     */
    public void updateApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    /**
     * Sends an HTTP request to the Dify API.
     *
     * @param route      The API route to send the request to.
     * @param formatArgs Format arguments for route URL placeholders.
     * @param body       The request body, if applicable.
     * @return The HTTP response containing the result of the API request.
     * @throws DifyClientException If an error occurs while sending the request.
     */
    public Response sendRequest(DifyRoute route, String[] formatArgs, RequestBody body) throws DifyClientException {
        try {
            String formattedURL = (formatArgs != null && formatArgs.length > 0)
                    ? String.format(route.url, (Object[]) formatArgs)
                    : route.url;

            Request request = new Request.Builder()
                    .url(baseUrl + formattedURL)
                    .method(route.method, body)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new DifyRequestException("Request failed with status: " + response.code());
            }
            return response;
        } catch (IOException e) {
            throw new DifyClientException("Error occurred while sending request: " + e.getMessage());
        }
    }


    /**
     * Sends a message feedback to the Dify API.
     *
     * @param messageId The ID of the message to provide feedback for.
     * @param rating    The feedback rating.
     * @param user      The user providing the feedback.
     * @return The HTTP response containing the result of the API request.
     * @throws DifyClientException If an error occurs while sending the request.
     */
    public Response messageFeedback(String messageId, String rating, String user) throws DifyClientException {
        JSONObject json = new JSONObject();
        json.put("rating", rating);
        json.put("user", user);

        return sendRequest(FEEDBACK, new String[]{messageId}, createJsonPayload(json));
    }

    /**
     * Retrieves application parameters from the Dify API.
     *
     * @param user The user for whom the application parameters are retrieved.
     * @return The HTTP response containing the result of the API request.
     * @throws DifyClientException If an error occurs while sending the request.
     */
    public Response getApplicationParameters(String user) throws DifyClientException {
        return sendRequest(APPLICATION, new String[]{user}, null);
    }

    /**
     * Creates a request body with the given JSON object.
     *
     * @param jsonObject The JSON object to be used in the request body.
     * @return The created request body.
     */
    RequestBody createJsonPayload(JSONObject jsonObject) {
        return RequestBody.create(jsonObject.toJSONString(), MediaType.parse("application/json"));
    }
}
