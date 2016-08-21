package test.tigerMoon.service;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberFactory;
import co.paralleluniverse.fibers.FiberUtil;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.tigerMoon.resource.FiberResource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by tiger on 16-7-28.
 */

@Service
public class FiberService {

    @Autowired
    private FiberResource fiberResource;

    public Object getClientData() {
        // here is an example of concurrency
        Fiber<Object> fiber1 = fiberResource.getServiceData();
        Fiber<Object> fiber2 = fiberResource.getServiceData();
        Fiber<Object> fiber3 = fiberResource.getServiceData();

        try {
            fiber1.get();
            fiber1.get();

            return FiberUtil.get(20, TimeUnit.SECONDS,fiber1,fiber2,fiber3);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
    * return a channel from fiber.this is not elegant
    * */
    public Object getClientDataWithChanel(){

        Fiber<Channel<Object>> serviceDataWithChannel = fiberResource.getServiceDataWithChannel();

        Fiber<Object> channelFiber = new Fiber<Object>() {
            protected Object run() throws SuspendExecution, InterruptedException {
                try {
                    return serviceDataWithChannel.get().receive();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        }.start();

        try {
            return  channelFiber.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return  null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }

    }


    public Object getChannelDataElegant(){
        Channel<Object> objectChannel = Channels.newChannel(100);
        fiberResource.getServiceDataWithChannel(objectChannel);

        Fiber<Object> fiber = new Fiber<Object>() {
            protected Object run() throws SuspendExecution, InterruptedException {
                return objectChannel.receive();
            }
        }.start();

        try {
            return fiber.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }




    public Object getServerData() {
        Map<String,String> map =new HashMap<>();
        map.put("foo","bar");
        return map;
    }
}
