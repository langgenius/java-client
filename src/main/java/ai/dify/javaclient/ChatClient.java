package ai.dify.javaclient;

import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a client for interacting with the Dify Chat API.
 * It provides methods for creating, retrieving, and managing chat messages and conversations.
 */
public class ChatClient extends DifyClient {

    /**
     * Constructs a new ChatClient with the provided API key.
     *
     * @param apiKey The API key to use for authentication.
     */
    public ChatClient(String apiKey) {
        super(apiKey);
    }

    /**
     * Constructs a new ChatClient with the provided API key and base URL.
     *
     * @param apiKey   The API key to use for authentication.
     * @param baseUrl  The base URL of the Dify API.
     */
    public ChatClient(String apiKey, String baseUrl) {
        super(apiKey, baseUrl);
    }

    /**
     * Generates query parameters in the form of key-value pairs joined by "&".
     *
     * @param params The map of query parameter key-value pairs.
     * @return A string representation of the generated query parameters.
     */
    private String generateQueryParams(Map<String, String> params) {
        List<String> keyValuePairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            keyValuePairs.add(entry.getKey() + "=" + entry.getValue());
        }
        return String.join("&", keyValuePairs);
    }

    /**
     * Creates a new chat message.
     *
     * @param inputs         The chat message inputs.
     * @param query          The query associated with the chat message.
     * @param user           The user associated with the chat message.
     * @param stream         Whether to use streaming response mode.
     * @param conversation_id The ID of the conversation, if applicable.
     * @return The HTTP response containing the result of the API request.
     * @throws DifyClientException If an error occurs while sending the request.
     */
    public Response createChatMessage(String inputs, String query, String user, boolean stream, String conversation_id) throws DifyClientException {
        JSONObject json = new JSONObject();
        JSONObject inputsJson = new JSONObject();
        inputsJson.put("query", query);
        json.put("inputs", inputsJson);
        json.put("user", user);
        json.put("response_mode", stream ? "streaming" : "blocking");
        if (conversation_id != null && !conversation_id.isEmpty()) {
            json.put("conversation_id", conversation_id);
        }

        return sendRequest(CREATE_CHAT_MESSAGE, null, createJsonPayload(json));
    }

    /**
     * Retrieves conversation messages.
     *
     * @param user           The user associated with the conversation.
     * @param conversation_id The ID of the conversation.
     * @param first_id       The ID of the first message to start fetching from.
     * @param limit          The maximum number of messages to retrieve.
     * @return The HTTP response containing the result of the API request.
     * @throws DifyClientException If an error occurs while sending the request.
     */
    public Response getConversationMessages(String user, String conversation_id, String first_id, int limit) throws DifyClientException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("user", user);

        if (conversation_id != null) {
            queryParams.put("conversation_id", conversation_id);
        }
        if (first_id != null) {
            queryParams.put("first_id", first_id);
        }
        if (limit > 0) {
            queryParams.put("limit", String.valueOf(limit));
        }
        String formattedQueryParams = generateQueryParams(queryParams);

        return sendRequest(GET_CONVERSATION_MESSAGES, new String[] {formattedQueryParams}, null);
    }

    /**
     * Retrieves conversations.
     *
     * @param user           The user associated with the conversations.
     * @param first_id       The ID of the first conversation to start fetching from.
     * @param limit          The maximum number of conversations to retrieve.
     * @param pinned         The pinned status of conversations to retrieve.
     * @return The HTTP response containing the result of the API request.
     * @throws DifyClientException If an error occurs while sending the request.
     */
    public Response getConversations(String user, String first_id, int limit, String pinned) throws DifyClientException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("user", user);
        if (first_id != null && !first_id.isEmpty()) {
            queryParams.put("first_id", first_id);
        }
        if (limit > 0) {
            queryParams.put("limit", String.valueOf(limit));
        }
        if (pinned != null && !pinned.isEmpty()) {
            queryParams.put("pinned", pinned);
        }
        String formattedQueryParams = generateQueryParams(queryParams);
        return sendRequest(GET_CONVERSATIONS, new String[] {formattedQueryParams}, null);
    }

    /**
     * Renames a conversation.
     *
     * @param conversation_id The ID of the conversation to rename.
     * @param name            The new name for the conversation.
     * @param user            The user associated with the conversation.
     * @return The HTTP response containing the result of the API request.
     * @throws DifyClientException If an error occurs while sending the request.
     */
    public Response renameConversation(String conversation_id, String name, String user) throws DifyClientException {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("user", user);

        return sendRequest(RENAME_CONVERSATION, new String[]{conversation_id}, createJsonPayload(json));
    }

    /**
     * Deletes a conversation.
     *
     * @param conversation_id The ID of the conversation to delete.
     * @param user            The user associated with the conversation.
     * @return The HTTP response containing the result of the API request.
     * @throws DifyClientException If an error occurs while sending the request.
     */
    public Response deleteConversation(String conversation_id, String user) throws DifyClientException {
        JSONObject json = new JSONObject();
        json.put("user", user);

        return sendRequest(DELETE_CONVERSATION, new String[]{conversation_id}, createJsonPayload(json));
    }
}

