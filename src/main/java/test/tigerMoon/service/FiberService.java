package test.tigerMoon.service;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.tigerMoon.resource.FiberResource;

import java.util.HashMap;
import java.util.Map;
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
            return FiberUtil.get(20, TimeUnit.SECONDS,fiber1,fiber2,fiber3);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getServerData() {
        Map<String,String> map =new HashMap<>();
        map.put("foo","bar");
        return map;
    }
}
