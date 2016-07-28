package test.tigerMoon.resource;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by tiger on 16-7-28.
 */
@Service
public class FiberResource {
    private static final Logger logger = LoggerFactory.getLogger(FiberResource.class);

    @Autowired
    private RestTemplate restTemplate;

    public Fiber<Object> getServiceData() {
        return new Fiber<Object>() {
            protected Object run() throws SuspendExecution, InterruptedException {
                logger.info("fiber get data. fiberId:{} ", this.getId());
                return getData();
            }
        }.start();
    }

    /**
     * when call other method in fiber. it should be suspendable
     */
    @Suspendable
    private Object getData() {
        String url = "http://localhost:8080/api/service/data";
        return restTemplate.getForEntity(url, Object.class).getBody();
    }
}
