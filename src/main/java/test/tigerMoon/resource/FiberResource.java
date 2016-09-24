package test.tigerMoon.resource;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
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
                this.sleep(5000);
                logger.info("sleep over ");
                return getData();
            }
        }.start();
    }


    public Fiber<Channel<Object>> getServiceDataWithChannel() {
        return new Fiber<Channel<Object>>() {
            protected Channel<Object> run() throws SuspendExecution, InterruptedException {
                logger.info("fiber get data. fiberId:{} ", this.getId());
                Object returnData = getData();
                Channel<Object> objectChannel = Channels.newChannel(100);
                objectChannel.send(returnData);
                return objectChannel;
            }
        }.start();
    }

    public Fiber<Void> getServiceDataWithChannel(Channel<Object> objectChannel) {
        return new Fiber<Void>((SuspendableRunnable) () -> {
            logger.info("fiber get data. ");
            Object returnData = getData();
            objectChannel.send(returnData);
        }).start();
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
