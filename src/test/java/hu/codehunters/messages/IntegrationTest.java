
package hu.codehunters.messages;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import hu.codehunters.messages.controller.MessageController;
import hu.codehunters.messages.service.MessageService;
import hu.codehunters.messages.service.ReportService;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {IntegrationTest.Config.class, MessageController.class, MessageService.class, ReportService.class})
public class IntegrationTest {//todo write tests
   /* private static final String TEST_SERVER_HOST = "localhost";

    static String getMockServerUrl(final int port) {
        return "http://" + TEST_SERVER_HOST + ":" + port;
    }
*/
    @TestConfiguration
    static class Config {
      /*  @Bean
        public Environment environment() {
            MockEnvironment env = new MockEnvironment();
            env.setProperty("external.machineLearning.http.url",
                    getMockServerUrl(WIRE_MOCK_CLASS_RULE.port()) + POST_MACHINE_LEARNING_ENDPOINT);
            env.setProperty("external.machineLearning.process.periodInHour", "24");
            return env;
        }*/

    }

    @Test
    public void contextTest(){}
/*
    private static final String POST_MACHINE_LEARNING_ENDPOINT = "/machineLearningEndpoint";

    private static final WireMockConfiguration WIRE_MOCK_CONFIGURATION = WireMockConfiguration.options().dynamicPort();


    @ClassRule
    public static WireMockClassRule WIRE_MOCK_CLASS_RULE = new WireMockClassRule(WIRE_MOCK_CONFIGURATION);

    @Rule
    public final WireMockClassRule wireMockRule = WIRE_MOCK_CLASS_RULE;
    @BeforeEach
    public void setup() {
        WireMock.configureFor(TEST_SERVER_HOST, WIRE_MOCK_CLASS_RULE.port());
        mockEndpoints();
    }

    private void mockEndpoints() {
        WireMock.stubFor(WireMock.post(POST_MACHINE_LEARNING_ENDPOINT).willReturn(ok()));
    }

    @Test
    public void testProcessWhenMachineLearningEndpointIsUp() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(POST_MACHINE_LEARNING_ENDPOINT);
        request.addHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity("[{\"message\":\"test message\"}]");
        request.setEntity(entity);
        httpClient.execute(request, resp ->
        {
            EntityUtils.consume(request.getEntity());
            assertEquals(200, resp.getCode());
            return resp;
        });
        verify(postRequestedFor(urlEqualTo(POST_MACHINE_LEARNING_ENDPOINT))
                .withHeader("Content-Type", equalTo("application/json")));

    }*/

}
