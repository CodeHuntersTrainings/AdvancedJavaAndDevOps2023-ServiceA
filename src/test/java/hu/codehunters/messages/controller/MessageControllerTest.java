package hu.codehunters.messages.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Queue;
import java.util.concurrent.*;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void flowTest() throws Exception {

        // checks that at startup the messages is empty
        mvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));

        // adds a message
        mvc.perform(MockMvcRequestBuilders
                        .post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"test message\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // checks that the message has been returned
        mvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[{\"message\":\"test message\"}]")));
    }

    @Test
    public void manyCallsTest() throws Exception {
        // checks that at startup the messages is empty
        mvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));

        final int messageCount = 100;
        StringBuilder sb = new StringBuilder("[");

        // adds many messages
        for (int i = 0; i < messageCount; i++) {
            String testMessage = "{\"message\": \"test message" + i + "\"}";

            mvc.perform(MockMvcRequestBuilders
                            .post("/messages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testMessage)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            sb.append(i < messageCount - 1 ? testMessage + "," : testMessage);
        }
        sb.append("]");
        // checks that the message has been returned
        String testMessages = sb.toString().replaceAll(": ", ":");
        mvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().json(testMessages));
    }

    @Test
    public void asyncPostMessageCallsTest() throws Exception {
        // checks that at startup the messages is empty
        mvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("[]")));

        final int messageCount = 10;
        Queue<String> messagesQueue = new LinkedBlockingQueue<>();
        StringBuilder sb = new StringBuilder("[");

        // adds many messages
        for (int i = 0; i < messageCount; i++) {
            String testMessage = "{\"message\": \"test message" + i + "\"}";
            messagesQueue.offer(testMessage);
            sb.append(i < messageCount - 1 ? testMessage + "," : testMessage);
        }

        Runnable postMessage = () -> {
            try {
                while (!messagesQueue.isEmpty()) {
                    mvc.perform(MockMvcRequestBuilders
                                    .post("/messages")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(messagesQueue.isEmpty() ? "{}" : messagesQueue.poll())
                                    .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        ExecutorService es = Executors.newFixedThreadPool(20);
        Future<?> posting = es.submit(postMessage);
        Future<?> posting2 = es.submit(postMessage);

        posting.get();
        posting2.get();

        // checks that the message has been returned
        String testMessages = sb.append("]").toString().replaceAll(": ", ":");
        mvc.perform(MockMvcRequestBuilders.get("/messages"))
                .andExpect(status().isOk())
                .andExpect(content().json(testMessages));
    }

    @Test
    public void testStats() throws Exception {
        // add messages
        mvc.perform(MockMvcRequestBuilders
                        .post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"test message1\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                        .post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"test:message2\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        String stats="{\"posted_messages\": 2,\n" +
                "  \"average_length\": 6.0,\n" +
                "  \"occurrences\": [\n" +
                "    {\"word\": \"test\", \"count\": 2},\n" +
                "    {\"word\": \"message1\", \"count\": 1},\n" +
                "    {\"word\": \"message2\", \"count\": 1}\n" +
                "  ]\n" +
                "}";
        mvc.perform(MockMvcRequestBuilders.get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().json((stats)));
    }

}
