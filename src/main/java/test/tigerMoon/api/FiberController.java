package test.tigerMoon.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import test.tigerMoon.service.FiberService;

/**
 * Created by tiger on 16-7-28.
 */

@RestController
@RequestMapping("/api")
public class FiberController {

    @Autowired
    private FiberService fiberService;

    @RequestMapping(value = "/service/data", method = RequestMethod.GET)
    public DeferredResult<Object> getServerData() {
        DeferredResult<Object> deferredResult = new DeferredResult<>();
        Object result = fiberService.getServerData();
        deferredResult.setResult(result);
        return deferredResult;
    }

    @RequestMapping(value = "/client/data", method = RequestMethod.GET)
    public DeferredResult<Object> getClientData() {
        DeferredResult<Object> deferredResult = new DeferredResult<>();
        Object result = fiberService.getClientData();
        deferredResult.setResult(result);
        return deferredResult;
    }

    @RequestMapping(value = "/client/data/channel", method = RequestMethod.GET)
    public DeferredResult<Object> getClientDataChanel() {
        DeferredResult<Object> deferredResult = new DeferredResult<>();
        Object result = fiberService.getChannelDataElegant();
        deferredResult.setResult(result);
        return deferredResult;
    }
}
