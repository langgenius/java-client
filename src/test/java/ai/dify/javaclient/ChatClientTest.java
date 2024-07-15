package ai.dify.javaclient;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * This class contains unit tests for the ChatClient class, focusing on its methods and interactions.
 */
class ChatClientTest {

    private static final String TEST_API_KEY = "testApiKey";
    private static final String TEST_BASE_URL = "http://testBaseUrl";
    private ChatClient chatClient;

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

    /**
     * Sets up the test environment before each test case.
     *
     * @throws IOException If an I/O error occurs.
     */
    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        chatClient = new ChatClient(TEST_API_KEY, TEST_BASE_URL);

        // Inject mock OkHttpClient using reflection
        try {
            java.lang.reflect.Field clientField = DifyClient.class.getDeclaredField("client");
            clientField.setAccessible(true);
            clientField.set(chatClient, mockClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests the createChatMessage method of the ChatClient class.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testCreateChatMessage() throws Exception {
        when(mockResponse.isSuccessful()).thenReturn(true);

        chatClient.createChatMessage("{}", "testQuery", "testUser", true, "conversation123");

        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }

    /**
     * Tests the getConversationMessages method of the ChatClient class.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testGetConversationMessages() throws Exception {
        when(mockResponse.isSuccessful()).thenReturn(true);

        chatClient.getConversationMessages("testUser", "conversation123", "firstId123", 5);

        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }


    /**
     * Tests the getConversations method of the ChatClient class.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testGetConversations() throws Exception {
        when(mockResponse.isSuccessful()).thenReturn(true);

        chatClient.getConversations("testUser", "firstId123", 5, "yes");

        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }

    /**
     * Tests the renameConversation method of the ChatClient class.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testRenameConversation() throws Exception {
        when(mockResponse.isSuccessful()).thenReturn(true);

        chatClient.renameConversation("conversation123", "newName", "testUser");

        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }

    /**
     * Tests the deleteConversation method of the ChatClient class.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testDeleteConversation() throws Exception {
        when(mockResponse.isSuccessful()).thenReturn(true);

        chatClient.deleteConversation("conversation123", "testUser");

        verify(mockClient).newCall(any(Request.class));
        verify(mockCall).execute();
    }

}
